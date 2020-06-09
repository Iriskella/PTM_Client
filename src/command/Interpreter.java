
package command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Will interpret a made up coding language given by our lecturer.
 */
public class Interpreter {
    //Data
    static volatile boolean isOn = false;
    static HashMap<String, Command> cMap = new HashMap<String, Command>(); // Commands Map
    static boolean isMapFull = false; // Fill once
    HashMap<String, Varible> varTable;//Variables table
    ArrayList<String> code; //code to interpret
    private int returnValue;

    //Ctor
    public Interpreter() {
        this.varTable = new HashMap<String, Varible>();
        this.code = new ArrayList<String>();
        if (!isMapFull) this.fillCMap();
        this.returnValue = 0;

        String[] startCode = {
        		"openDataServer 5000 10",
                "var breaks = bind /controls/flight/speedbrake",
                "var throttle = bind /controls/engines/current-engine/throttle",
                "var heading = bind /instrumentation/heading-indicator/indicated-heading-deg",
                "var airspeed = bind /instrumentation/airspeed-indicator/indicated-speed-kt",
                "var roll = bind /instrumentation/attitude-indicator/indicated-roll-deg",
                "var pitch = bind /instrumentation/attitude-indicator/internal-pitch-deg",
                "var rudder = bind /controls/flight/rudder",
                "var aileron = bind /controls/flight/aileron",
                "var elevator = bind /controls/flight/elevator",
                "var alt = bind /instrumentation/altimeter/indicated-altitude-ft",
                "var rpm = bind /engines/engine/rpm",
                "var hroute = 0",
                "var goal = 0",
                "var altr = 2000",
                "var e=0",
                "var r=0"
        };
        this.interpret(startCode);
        this.stop();
    }

    //Public methods
    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * Interpret by a given string array
     *
     * @param lines Code to interpret world by world
     */
    public int interpret(String[] lines) {
        isOn = true;
        this.lexer(lines);
        this.parser();
        return returnValue;
    }

    /**
     * Interpert by a given file
     *
     * @param fileName File that contains code to interpret
     */
    public int interpret(String fileName) {
        isOn = true;
        this.lexer(fileName);
        this.parser();
        return returnValue;
    }

    public void stop() {
        isOn = false;
        cMap.get("disconnect").execute(this, 1);
    }

    private void parser() {
        // Function parse the code array list and execute the commands
        for (int index = 0; index < this.code.size(); index++) {
            if (!isOn) return;
            Command c = cMap.get(this.code.get(index));
            if (c != null) {
                index += c.execute(this, index);
            } else if (this.code.get(index).contains("=")) {
                String[] s = this.code.get(index).split(String.format("((?<=%1$s)|(?=%1$s))", "[-=+/*]"));
                this.code.remove(index);
                for (int j = s.length - 1; j >= 0; j--)
                    this.code.add(index, s[j]);
            }
        }
    }

    private void lexer(String fileName) {
        // Function reads the file line by line and fill code array list
        try {
            String[] lines = Files.lines(Paths.get("./resources/" + fileName)).toArray(String[]::new);
            this.lexer(lines);
        } catch (IOException e) {
        }
    }

    private void lexer(String[] lines) {
        // Function reads from String line by line and fill code array list
        this.code = new ArrayList<>();
        for (String s : lines) {
            String[] temp = s.split(" ");
            for (String s2 : temp)
                this.code.add(s2);
        }
    }

    private void fillCMap() {
        //Function fills cMap (static map) and update isMapFull
        Interpreter.isMapFull = true;
        Interpreter.cMap.put("openDataServer", new OpenDataServerCommand());
        Interpreter.cMap.put("connect", new ConnectCommand());
        Interpreter.cMap.put("var", new DefineVarCommand());
        Interpreter.cMap.put("=", new EqualsCommand());
        Interpreter.cMap.put("print", new PrintCommand());
        Interpreter.cMap.put("if", new IfCommand());
        Interpreter.cMap.put("while", new LoopCommand());
        Interpreter.cMap.put("sleep", new SleepCommand());
        Interpreter.cMap.put("disconnect", new DisconnectCommand());
        Interpreter.cMap.put("return", new ReturnCommand());
    }

    //-----------------------------------------------Class check-----------------------------------------------------------
    public static void main(String[] args) {
        Interpreter i = new Interpreter();
        String[] lines = {
                "breaks = 0",
                "throttle = 1",
                "var h = heading",
                "var minus = -1",
                "var a = alt",
                "sleep 400",
                "print start",
                "while a - alt > -50 {",
                "rudder = ( h - heading )/20",
                "print rudder",
                "elevator = pitch / 50",
                "print elevator",
                "print alt",
                "sleep 50",
                "}"
        };

        i.interpret(lines);
    }
}
