package model;

import command.Interpreter;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ConnectionPendingException;
import java.util.Observable;

public class MainModel extends Observable {

    private SimulatorConnection singleConnection;
    private PathFinderConnection pathFinderConnection;
    private final Interpreter interpreter = new Interpreter();
    private Thread thread;

    // ----------------------------------- Simulator functions -------------------------------------------

    /**
     * Connect to the simulator server application with the given IP and port
     * Simulator Connection can be established once
     *
     * @param ip   simulator server ip
     * @param port simulator application port
     */
    public void connectToASimulator(String ip, int port) {
        singleConnection = SimulatorConnection.getConnection(ip, port);
    }

    public void disconnectFromTheSimulator(){
        singleConnection.stopAndDeleteConnection();
    }

    /**
     * Send move commands to the simulator
     *
     * @param aileron  right/left value
     * @param elevator up/down value
     */
    public void movePlain(double aileron, double elevator) {
        String[] commands = {
                "set /controls/flight/aileron " + aileron,
                "set /controls/flight/elevator " + elevator,
        };
        singleConnection.sendData(commands);
    }

    public void setThrottle(double throttle) {
        String[] commands = {
                "set /controls/engines/current-engine/throttle " + throttle
        };
        singleConnection.sendData(commands);
    }

    public void setRudder(double rudder) {
        String[] commands = {
                "set /controls/flight/rudder " + rudder
        };
        singleConnection.sendData(commands);
    }

    /**
     * Ask the simulator for the plain current location every 2 seconds from lunch
     */
    public void getPlainLocation() {
        new Thread(()->{
            while(true) {
                setChanged();
                notifyObservers(singleConnection.getPlainLocation());
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    // ----------------------------------- PathFinder functions -------------------------------------------

    /**
     * Connect to the Path Finder server application with the given IP and port
     *
     * @param ip   PathFinder server ip
     * @param port PathFinder application port
     */
    public void connectToAPathFinder(String ip, int port) {
        pathFinderConnection = new PathFinderConnection(ip, port);
    }

    /**
     * Calculate lightest path. Path Finder server connection
     * have to be established before using this function
     *
     * @param startPos Path Finder from
     * @param endPos   Path Finder to
     * @param map      map represented by a matrix
     * @return result as a string array ( example result: up , down , right ...)
     * @throws IOException
     */
    public void findPath(Point startPos, Point endPos, int[][] map) throws IOException {
        if (pathFinderConnection == null) throw new ConnectionPendingException();
        pathFinderConnection.sendProblem(startPos, endPos, map);
        String solution[] = pathFinderConnection.receiveSolution();
        this.setChanged();
        this.notifyObservers(solution);
    }

    // ----------------------------------- AutoPilot functions -------------------------------------------
    public void interpret(File aFile) {
        thread = new Thread(() -> interpreter.interpret(aFile.getName()));
        try{
            thread.start();
        }catch (Exception e) {}
    }

    public void stopInterpret() {
        thread.interrupt();
        interpreter.stop();
    }

}
