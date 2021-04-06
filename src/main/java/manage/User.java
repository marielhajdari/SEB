package manage;
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
        this._username = "username";
        this._pwd = "pwd";
        this._elo = 100;
        this._isLogged = false;
        this._participating = false;
    }

    public String get_username() {
        return _username;
    }

    public void registerUser(String username, String pwd) {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement ps;
            ps = conn.prepareStatement("SELECT COUNT(username) FROM users WHERE username = ?;");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            ps.close();
            if (!rs.next() || rs.getInt(1) > 0){
                return;
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
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
}
