package server;

import manage.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class RespondRequest {

    User u = new User();
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
            /*case 3 -> savePackage(_payload);
            case 4 -> buyPackage();
            case 5 -> showStack();
            case 6 -> showDeck();
            case 7 -> configureDeck();
            case 8 -> showDeckOther();
            case 9 -> showUserData();
            */
        }
        _out.flush();
    }
}
