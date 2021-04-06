package manage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class User {
    private String _username;
    private String _pwd;
    private int _elo;
    private boolean _isLogged;
    private boolean _participating;

    public User() {
        this._username = "mariel";
        this._pwd = "pwd";
        this._elo = 100;
        this._isLogged = false;
        this._participating = false;
    }

    public String get_username() {
        return _username;
    }

    public boolean registerUser(String username, String pwd) {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps;
            ps = conn.prepareStatement("SELECT COUNT(username) FROM users WHERE username = ?;");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            ps.close();
            if (!rs.next() || rs.getInt(1) > 0){
                System.out.println("User already registered!!!");
                return false;
            }
            // no admin rights/ no token security (yet)
            /*if (username.equals("admin")){
                ps = conn.prepareStatement("INSERT INTO users(username, pwd, token, admin) VALUES(?,?,?,TRUE);");
            } else {
                ps = conn.prepareStatement("INSERT INTO users(username, pwd, token) VALUES(?,?,?);");
            }*/
            ps = conn.prepareStatement("INSERT INTO users(username, pwd) VALUES(?,?);");
            ps.setString(1, username);
            ps.setString(2, pwd);
            int affectedRows = ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loginUser(String username, String pwd){
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET logged = TRUE WHERE username = ? AND pwd = ?;");
            ps.setString(1, username);
            ps.setString(2, pwd);
            int affectedRows = ps.executeUpdate();
            ps.close();
            conn.close();
            if (affectedRows == 1) {
                System.out.println("User is logged!!!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean logoutUser(String username, String pwd){
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET logged = FALSE WHERE username = ? AND pwd = ?;");
            ps.setString(1, username);
            ps.setString(2, pwd);
            int affectedRows = ps.executeUpdate();
            ps.close();
            conn.close();
            if (affectedRows == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean logoutALLUsers(){
        try{
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET logged = FALSE WHERE logged = TRUE;");
            ps.executeUpdate();

            ps.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean battleWon(){
        _elo+=2;
        return saveStats();
    }
    public boolean battleLost(){
        _elo-=1;
        return saveStats();
    }
    public boolean battleDraw(){
        _elo++;
        return saveStats();
    }
    public boolean saveStats(){
        try {
            //DBConnection conn = DBConnection.getInstance().getConnection();
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("UPDATE users SET elo = ? WHERE username = ?;");
            ps.setInt(1,_elo);
            ps.setString(2,_username);
            ps.executeUpdate();
            ps.close();
            db.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String showUserStats (String uname){
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT SUM(countPushUps), usr_name, sum(users.elo)/count(users.elo) FROM history join users on usr_name = ? group by usr_name;");
            ps.setString(1,uname);
            String json = result2Json(ps.executeQuery());
            ps.close();
            conn.close();
            return json;
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String score (){
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT SUM(countPushUps), usr_name, sum(users.elo)/count(users.elo) FROM history join users on usr_name = users.username group by usr_name;");
            String json = result2Json(ps.executeQuery());
            ps.close();
            conn.close();
            return json;
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String showHistory(String uname){
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM history where usr_name = ?;");
            ps.setString(1,uname);
            String json = resultHistory2Json(ps.executeQuery());
            ps.close();
            conn.close();
            return json;
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String result2Json(ResultSet rs) throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        while (rs.next()){
            ObjectNode on = mapper.createObjectNode();
            on.put("TotalPushUps:",rs.getInt(1));
            //on.put("Elo:",rs.getInt(2));
            on.put("Name:",rs.getString(2));
            on.put("Elo:",rs.getInt(3));
            arrayNode.add(on);
        }
        rs.close();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
    }

    private String resultHistory2Json(ResultSet rs) throws SQLException, JsonProcessingException {
        ObjectMapper mapp = new ObjectMapper();
        ArrayNode aNode = mapp.createArrayNode();
        while (rs.next()){
            ObjectNode on = mapp.createObjectNode();
            on.put("EntryID:",rs.getInt(1));
            //on.put("Elo:",rs.getInt(2));
            on.put("PushUps:",rs.getInt(2));
            on.put("Duration:",rs.getInt(3));
            on.put("Name:",rs.getString(4));
            aNode.add(on);
        }
        rs.close();
        return mapp.writerWithDefaultPrettyPrinter().writeValueAsString(aNode);
    }
}
