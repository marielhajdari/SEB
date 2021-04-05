package manage;

//import com.fasterxml.jackson.core.JsonProcessingException;
import database.DBConnection;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import manage.Tournament;

public class History {
    private int _entryid = 0;
    private int _countPushUps;
    private int _duration;

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
    public boolean addEntry(User user, int pushUps, int duration) {
        try {
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("INSERT into history(countPushUps, durationsInSeconds, usr_name) VALUES(?,?,?)");
            ps.setInt(1, pushUps);
            ps.setInt(2, duration);
            ps.setString(3, user.get_username());
            int affectedRows = ps.executeUpdate();
            ps.close();
            db.close();
            if (affectedRows == 1) {
                if (!findActiveTour()){
                    Tournament tr = new Tournament();
                    addParticipant(user);
                }else {
                    addParticipant(user);
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean findActiveTour(){
        try{
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("SELECT COUNT(isActive) FROM tournament WHERE isActive = TRUE;");
            int activeTour = ps.executeUpdate();
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

    public void addParticipant(User user){
        try{
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("INSERT INTO tourParticipants(totalPushUps, participantName, tourid) VALUES(?,?,?) ");
            ps.setInt(1, totalPushUps(user));
            ps.setString(2, user.get_username());
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

    public int totalPushUps(User user){
        try{
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("SELECT SUM(countPushUps) FROM history WHERE usr_name = ?;");
            ps.setString(1, user.get_username());
            int totalPushUps = ps.executeUpdate();
            ps.close();
            db.close();
            return totalPushUps;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
