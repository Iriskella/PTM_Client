package viewModel;

import command.Interpreter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import model.MainModel;
import model.Point;
import view.MainWindowController;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {
    final MainWindowController view;
    final MainModel mainModel;
    public DoubleProperty aileron, elevator, rudder, throttle;

    // Constructor
    public ViewModel(MainWindowController view, MainModel mainModel) {
        this.view = view;
        this.mainModel = mainModel;
        aileron = new SimpleDoubleProperty();
        elevator = new SimpleDoubleProperty();
        rudder = new SimpleDoubleProperty();
        throttle = new SimpleDoubleProperty();
    }

    // ----------------------------------- Simulator functions -------------------------------------------
    /**
     * Connect to the simulator server application with the given IP and port
     * Simulator Connection can be established once
     *
     * @param ip   simulator server ip
     * @param port simulator application port
     */
    public void connectToSimulator(String ip, String port) {
        this.mainModel.connectToASimulator(ip, Integer.parseInt(port));
    }

    /**
     * Ask the simulator for the plain current location
     */
    public void getPlainLocation() {
        this.mainModel.getPlainLocation();
    }

    /**
     * Send move commands to the simulator
     */
    public void movePlain() {
        mainModel.movePlain(aileron.getValue(), elevator.getValue());
    }

    public void setThrottle() {
        mainModel.setThrottle(throttle.getValue());
    }

    public void setRudder() {
        mainModel.setRudder(rudder.getValue());
    }

    // ----------------------------------- PathFinder functions -------------------------------------------

    /**
     * Connect to the Path Finder server application with the given IP and port
     *
     * @param ip   PathFinder server ip
     * @param port PathFinder application port
     */
    public void connectToPathFinder(String ip, String port) {
        this.mainModel.connectToAPathFinder(ip, Integer.parseInt(port));
    }

    /**
     * Calculate lightest path. Path Finder server connection
     * have to be established before using this function
     *
     * @param startPos Path Finder from
     * @param endPos   Path Finder to
     * @param map      map represented by a matrix
     */
    public void findPath(Point startPos, Point endPos, int[][] map) {
        new Thread(() -> {
            try {
                this.mainModel.findPath(startPos, endPos, map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ----------------------------------- AutoPilot functions -------------------------------------------
    public void interpret(File aFile){
        this.mainModel.interpret(aFile);
    }
    public void stopInterpret(){
        this.mainModel.stopInterpret();
    }

    // ----------------------------------- Observer functions -------------------------------------------

    @Override
    public void update(Observable object, Object arg) {
        if (object != this.mainModel) return;

        String[] input = (String[]) arg;
        setChanged();
        notifyObservers(input);
    }



}
