package manage;

//import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.DBConnection;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import manage.Tournament;

public class History {
    private int _entryid = 0;
    private int _countPushUps;
    private int _duration;

    public History() {
        // do nothing
    }

    public History(int _countPushUps, int _duration) {
        _entryid++;
        this._countPushUps = _countPushUps;
        this._duration = _duration;
    }
    // getter and setter
    public int get_entryid() {
        return _entryid;
    }

    public int get_countPushUps() {
        return _countPushUps;
    }

    public int get_duration() {
        return _duration;
    }

    public void set_entryid(int _entryid) {
        this._entryid = _entryid;
    }

    public void set_countPushUps(int _countPushUps) {
        this._countPushUps = _countPushUps;
    }

    public void set_duration(int _duration) {
        this._duration = _duration;
    }

    // methods
    public void addEntry(String uname, int pushUps, int duration) {
        try {
            Connection db = DBConnection.getInstance().getConnection();
            addHistory(uname,pushUps,duration);
            PreparedStatement userParticipating = db.prepareStatement("SELECT count(participantName) from tourParticipants where participantName = ?;");
            userParticipating.setString(1, uname);
            ResultSet up = userParticipating.executeQuery();
            up.next();
            if (up.getInt(1) > 0){
                int newTotal = addPushUps(uname, pushUps);
                PreparedStatement ps2 = db.prepareStatement("Update tourParticipants Set totalPushUps = ? where participantName = ?;");
                ps2.setInt(1,newTotal);
                ps2.setString(2,uname);
                ps2.close();
                userParticipating.close();
            }else {
                PreparedStatement ps = db.prepareStatement("INSERT into history(countPushUps, durationInSeconds, usr_name) VALUES(?,?,?);");
                ps.setInt(1, pushUps);
                ps.setInt(2, duration);
                ps.setString(3, uname);
                int affectedRows = ps.executeUpdate();
                ps.close();
                db.close();
                if (affectedRows == 1) {
                    if (!findActiveTour()){
                        Tournament tr = new Tournament();
                        addParticipant(uname);
                        userParti(uname);
                    }else {
                        addParticipant(uname);
                        userParti(uname);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean findActiveTour(){
        try{
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("SELECT COUNT(isActive) FROM tournament WHERE isActive = TRUE;");
            ResultSet rs = ps.executeQuery();
            rs.next();
            int activeTour = rs.getInt(1);
            ps.close();
            db.close();
            if (activeTour != 0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addParticipant(String uname){
        try{
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("INSERT INTO tourParticipants(totalPushUps, participantName, tourid) VALUES(?,?,?);");
            ps.setInt(1, totalPushUps(uname));
            ps.setString(2, uname);
            ps.setInt(3, 1);
            // GETTTT tour ID
            //////
            /////
            /////
            ps.executeUpdate();
            ps.close();
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int totalPushUps(String uname){
        try{
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("SELECT SUM(countPushUps) FROM history WHERE usr_name = ?;");
            ps.setString(1, uname);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int totalPushUps = rs.getInt(1);

            ps.close();
            db.close();
            return totalPushUps;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void userParti(String uname){
        try {
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("UPDATE users SET participating = TRUE Where username = ?;");
            ps.setString(1, uname);
            ps.executeUpdate();
            ps.close();
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void userNotParti(String uname){
        try {
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("UPDATE users SET participating = False Where username = ?;");
            ps.setString(1, uname);
            ps.executeUpdate();
            ps.close();
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int addPushUps(String uname, int pu){
        try {
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("SELECT totalPushUps from tourParticipants where participantName = ?;");
            ps.setString(1, uname);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int newTotal = rs.getInt(1) + pu;
            ps.close();
            db.close();
            return  newTotal;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addHistory(String uname, int pu, int duration){
        try {
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("INSERT into history(countPushUps,durationInSeconds,usr_name) VALUES(?,?,?);");
            ps.setInt(1, pu);
            ps.setInt(2, duration);
            ps.setString(3, uname);
            ps.executeUpdate();
            ps.close();
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String tourScoreboard(){
        try {
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("SELECT * from tourParticipants order by totalPushUps desc");
            ResultSet rs = ps.executeQuery();
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode arrayNode = mapper.createArrayNode();
            while (rs.next()){
                ObjectNode on = mapper.createObjectNode();
                on.put("participant ID :",rs.getInt(1));
                //on.put("Elo:",rs.getInt(2));
                on.put("Name:",rs.getString(3));
                on.put("Total PushUps:",rs.getInt(2));
                on.put("Tour ID :",rs.getInt(4));
                arrayNode.add(on);
            }
            rs.close();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
