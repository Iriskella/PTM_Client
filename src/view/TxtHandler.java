package view;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TxtHandler {

    public static File LoadFile(Window window) {

        // Create a new file chooser window
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open txt file");
        fileChooser.setInitialDirectory(new File(".\\resources"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.
                ExtensionFilter("TEXT Files", "*.txt"));

        // Open file chooser window and return selected file
        return fileChooser.showOpenDialog(window);
    }

    public static String[] getTxtData(File txtFile) throws FileNotFoundException {
        if (txtFile == null) throw new FileNotFoundException();

        // Check file exception
        String fileName = txtFile.getName();
        int fileNameLength = fileName.length();
        if (!fileName.substring(fileNameLength - 3, fileNameLength).equals("txt")) throw new IllegalArgumentException();

        // Read from file

        Scanner scanner = new Scanner(txtFile);
        List<String> output = new ArrayList<>();
        while (scanner.hasNext())
            output.add(scanner.nextLine());
        scanner.close();

        // Return data
        return output.toArray(new String[0]);
    }

    // ---------------------------------------------  Class main ----------------------------------------------
    public static void main(String[] args) {
        try {
            File aFile = new File("./resources/takeoff.txt");
            String[] fileData = TxtHandler.getTxtData(aFile);
            int rowAmount = fileData.length;
            for (int row = 0; row < rowAmount; row++)
                System.out.println(fileData[row]);
        } catch (FileNotFoundException e) {
            System.out.println("File not exist");
        } catch (IllegalStateException e) {
            System.out.println("Wrong file extention");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
