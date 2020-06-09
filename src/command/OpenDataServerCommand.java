package command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Command : openDataServer port latency
 * execute : activate a server that listens to port and read lines in latency per second in another thread
 */
public class OpenDataServerCommand implements Command {
    //Data
    private int port, latency;
    private volatile static boolean stop;
    Interpreter interpreter;

    @Override
    public int execute(Interpreter i, int index) {
        this.interpreter = i;
        this.port = Integer.parseInt(this.interpreter.code.get(index + 1));
        this.latency = Integer.parseInt(this.interpreter.code.get(index + 2));
        stop = false;
        try {
            if (port < 1000 || port > 9999) throw new Exception();
        } catch (Exception e) {
        } //valibale port
        new Thread(() -> this.runServer()).start();//Run server on different thread
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        return 2;
    }

    //Private methods
    private void runServer() {
        try {
            ServerSocket server = new ServerSocket(port);//server is alive
			System.out.println(port);
            server.setSoTimeout(3000);
            while (!this.stop) {
                try {
                    Socket aClient = server.accept();//a client has connected to the server
                    try {
                        this.readLines(aClient.getInputStream());
                        aClient.close();//closing the client
                    } catch (IOException e) {
                        System.out.println("exit(3) = can't read from client");
                    } catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (SocketTimeoutException e) {
                    System.out.println("exit(2) = timeOut");
                }
            }
            server.close();//closing the server
        } catch (IOException e) { System.out.println("exit(1)");}// Coudn't connect to a socket
    }

    private void readLines(InputStream in) throws IOException, InterruptedException {
        String[] variablesNames = {"airspeed", "alt", "Pressure", "pitch", "roll", "Internal-Pitch",
                "Internal-Roll", "Encoder-Altitude", "Encoder-Pressure", "GPS-Altitude", "Ground-Speed",
                "Vertical-Speed", "heading", "Compass-Heading", "Slip", "Turn", "Fpm-Speed", "aileron",
                "elevator", "rudder", "Flaps", "throttle", "Rpm"
        };
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(in));
        String line;

        while (!stop) { // Reading line by line
            line = inFromClient.readLine();
            Thread.sleep(1000 / this.latency);// Latency times a second
            String[] variablesValues = line.split(",");
            for (int index = 0; index < variablesNames.length; index++) {
                String varName = variablesNames[index];
                double value = Double.parseDouble(variablesValues[index]);
                Varible var = this.interpreter.varTable.get(varName);
                if (var != null) //Update if already exist
                    var.setValueWithoutBind(value);
                else { //Create new if not
                    Varible v = new Varible(value);
                    this.interpreter.varTable.put(varName, v);
                }
            }
        }
        inFromClient.close();
    }

    /**
     * Stopping the server
     */
    public static void stop() {
        stop = true;
    }
}

//"/instrumentation/airspeed-indicator/indicated-speed-kt", "/instrumentation/altimeter/indicated-altitude-ft", "/instrumentation/altimeter/pressure-alt-ft",
//"/instrumentation/attitude-indicator/indicated-pitch-deg", "/instrumentation/attitude-indicator/indicated-roll-deg", "/instrumentation/attitude-indicator/internal-pitch-deg",
//"/instrumentation/attitude-indicator/internal-roll-deg","/instrumentation/encoder/indicated-altitude-ft", "/instrumentation/encoder/pressure-alt-ft",
//"/instrumentation/gps/indicated-altitude-ft", "/instrumentation/gps/indicated-ground-speed-kt",	"/instrumentation/gps/indicated-vertical-speed",
//"/instrumentation/heading-indicator/indicated-heading-deg", "/instrumentation/magnetic-compass/indicated-heading-deg","/instrumentation/slip-skid-ball/indicated-slip-skid",
//"/instrumentation/turn-indicator/indicated-turn-rate","/instrumentation/vertical-speed-indicator/indicated-speed-fpm", "/controls/flight/aileron",
//"/controls/flight/elevator","/controls/flight/rudder", "/controls/flight/flaps","/controls/engines/engine/throttle","/engines/engine/rpm"
