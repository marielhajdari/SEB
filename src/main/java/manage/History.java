package manage;

//import com.fasterxml.jackson.core.JsonProcessingException;
import database.DBConnection;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

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
    public boolean addEntry(User user) {
        try {
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("INSERT into history(countPushUps, durationsInSeconds, usr_name) VALUES(?,?,?)");
            ps.setInt(1, get_countPushUps());
            ps.setInt(2, get_duration());
            ps.setString(3, user.get_username());
            int affectedRows = ps.executeUpdate();
            ps.close();
            db.close();
            if (affectedRows == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }
}
