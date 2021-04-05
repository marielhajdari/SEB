package manage;


import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Tournament {
    private boolean isActive;

    public Tournament() {
        beginTournament();
    }

    public void beginTournament(){
        try{
            Connection db = DBConnection.getInstance().getConnection();
            PreparedStatement ps = db.prepareStatement("INSERT INTO tournament(isActive) VALUES(TRUE) ");
            ps.executeUpdate();
            ps.close();
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
