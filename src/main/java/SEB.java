import server.ReadRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SEB {

    public static ReadRequest reader = new ReadRequest();
    //public static Battlefield battle = new Battlefield();

    static ServerSocket _sSocket = null;
    static final int _port = 10001;

    public static void main(String[] args) throws IOException {
        System.out.println("srv: Starting server...");

        _sSocket = new ServerSocket(_port);
        System.out.println("srv: Server is running in port " + _port);

        // repeatedly wait for connections, and process
        // noinspection InfiniteLoopStatement
        while (true) {
            // connect to client
            Socket clientSocket = _sSocket.accept();
            System.out.println("srv: New client");
            reader = new ReadRequest(clientSocket);
            reader.readRequest();
            //close the client socket
            clientSocket.close();

        }
    }
}
