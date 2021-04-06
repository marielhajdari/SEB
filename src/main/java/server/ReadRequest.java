package server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReadRequest {
    //private Battlefield _battle;
    //private final PostGre _db = new PostGre();
    private BufferedReader _in;
    private StringBuilder _messageSeparator = new StringBuilder();
    private CURLY _myVerb = CURLY.NOT_VALID;
    private String _message;
    private String _payload;
    private String[] _command;
    private final Map<String, String> __header = new HashMap<>();
    private boolean _http_first_line = true;
    private Socket _clientSocket;

    //private final String[] _allowedReq = {"users", "sessions", "packages", "transactions", "cards", "deck", "stats", "score", "battles", "tradings", "deck?format=plain"};
    private final String[] _allowedReq = {"users", "sessions", "stats", "score", "history", "tournament",};

    public ReadRequest(){

    }



    public ReadRequest(Socket clientSocket) throws IOException {
        this._in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this._clientSocket = clientSocket;
    }

    protected void setMyCurl(String myCurl) {
        switch (myCurl) {
            case "GET" -> _myVerb = CURLY.GET;
            case "POST" -> _myVerb = CURLY.POST;
            case "PUT" -> _myVerb = CURLY.PUT;
            case "DELETE" -> _myVerb = CURLY.DELETE;
            default -> _myVerb = CURLY.NOT_VALID;
        }
    }

    private void separateMessage() {
        //separate message
        String[] request = _messageSeparator.toString().split(System.getProperty("line.separator"));
        _messageSeparator = new StringBuilder();
        boolean skip = true;
        for (String line : request) {
            if (!line.isEmpty()) {
                if (_http_first_line) {
                    //saving folder and version
                    String[] first_line = line.split(" ");
                    setMyCurl(first_line[0]);
                    _message = first_line[1];
//                    _version = first_line[2];
                    _http_first_line = false;
                } else {

                    //saving the header
                    if (line.contains(": ") && !line.contains("{")) {
                        String[] other_lines = line.split(": ");
                        __header.put(other_lines[0], other_lines[1]);
                    }
                    //saving the payload
                    else {
                        if (skip) {
                            skip = false;
                        } else {
                            _messageSeparator.append(line);
                            _messageSeparator.append("\r\n");
                        }

                    }
                }
            }
        }
        _payload = _messageSeparator.toString();
    }


    public void readRequest() throws IOException {
        //read and save request
        //save request
        while (_in.ready()) {
            _messageSeparator.append((char) _in.read());
        }
        separateMessage();
        int status = checkRequest();
        RespondRequest response = new RespondRequest(_clientSocket);
        response.performRequest(status);
        //performRequest(status);


    }


    //0 - error occurred
    //1 - create account
    //2 - log in
    //3 -
    //4 - edit user
    //5 - show user stats
    //6 - score
    //7 - show history
    //8 - tournament scoreboard
    //9 - add entry
    private int checkRequest() {

        //check if command is supported
        if(_myVerb == CURLY.NOT_VALID) {
            System.out.println("srv: Request method not supported");
        } else {
            _command = _message.split("/");
            if(Arrays.asList(_allowedReq).contains(_command[1])) {
                if (_command[1].equals("users") && _myVerb == CURLY.POST) {
                    return 1;
                }else if (_command[1].equals("sessions") && _myVerb == CURLY.POST) {
                    return 2;
                }else if (_command[1].equals("users") && _myVerb == CURLY.GET) {
                    return 3;
                }else if (_command[1].equals("users") && _myVerb == CURLY.PUT) {
                    return 4;
                }else if(_command[1].equals("stats") && _myVerb == CURLY.GET){
                    return 5;
                }else if(_command[1].equals("score") && _myVerb == CURLY.GET){
                    return 6;
                }else if(_command[1].equals("history") && _myVerb == CURLY.GET){
                    return 7;
                }else if(_command[1].equals("tournament") && _myVerb == CURLY.GET){
                    return 8;
                }else if(_command[1].equals("history") && _myVerb == CURLY.POST){
                    return 9;
                }

            }
        }
        return 0;
    }

}
