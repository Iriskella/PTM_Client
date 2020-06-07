package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Class represent a !single! connection to the simulator.
 * Class build around the Singleton design pattern idea
 */
public class SimulatorConnection {

    private static SimulatorConnection singleConnection = null;
    private Socket socket;
    private PrintWriter outToSocket;
    private BufferedReader inFromSocket;

    // Constructor, establish a single connection to the simulator
    private SimulatorConnection(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            outToSocket = new PrintWriter(socket.getOutputStream());
            inFromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Simulator connection established");
    }

    /**
     * Create instance of SimulatorConnection class
     *
     * @param ip   simulator server ip
     * @param port simulator application port
     * @return simulator connection
     */
    public static SimulatorConnection getConnection(String ip, int port) {
        if (singleConnection == null)
            singleConnection = new SimulatorConnection(ip, port);

        return singleConnection;
    }

    /**
     * return plain location
     */
    public String[] getPlainLocation() {

        // Request position
        outToSocket.println("dump /position");
        outToSocket.flush();

        // Read result
        String line;
        ArrayList<String> location = new ArrayList<>();
        try {
            while (!(line = inFromSocket.readLine()).equals("</PropertyList>")) {
                if (!line.equals(""))
                    location.add(line);
            }

            String longitude = location.get(2);
            String latitude = location.get(3);
            String[] x = longitude.split("[<>]");
            String[] y = latitude.split("[<>]");
            inFromSocket.readLine();
            return new String[]{x[2],y[2]};
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sending data to the simulator
     *
     * @param dataToSend list of string to pass to the simulator
     */
    public void sendData(String[] dataToSend) {
        for (String line : dataToSend) {
            outToSocket.println(line);
            outToSocket.flush();
        }
    }

    /**
     * Stop and delete current connection
     */
    public void stopAndDeleteConnection() {
        try {
            if (this.outToSocket != null)
                this.outToSocket.close();
            if (this.inFromSocket != null)
                this.inFromSocket.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        singleConnection = null;
    }
}
