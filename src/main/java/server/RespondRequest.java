package server;

import manage.History;
import manage.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class RespondRequest {

    User u = new User();
    History h = new History();



    private BufferedWriter _out;

    public RespondRequest(Socket clientSocket) throws IOException {
        this._out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }
    public void performRequest(int status) throws IOException {
        if (status == 0) {
            _out.write("HTTP/1.1 400\r\n");
            _out.write("Content-Type: text/html\r\n");
            _out.write("\r\n");
            _out.write("Bad request!\r\n");
        } else {
            _out.write("HTTP/1.1 200 OK\r\n");
            _out.write("Content-Type: text/html\r\n");
            _out.write("\r\n");
        }
        switch (status) {
            case 1 -> u.registerUser(u.get_username(),"1234");
            case 2 -> u.loginUser(u.get_username(), "1234");
            case 5 -> System.out.println(u.showUserStats("username"));
            case 6 -> System.out.println(u.score());
            case 7 -> System.out.println(u.showHistory("username"));
            case 9 -> h.addEntry("mariel", 20, 40);
            case 8 -> System.out.println(h.tourScoreboard());
            /*case 3 -> ;
            case 4 -> ;

            case 8 -> s;

            */
        }
        _out.flush();
    }
}
