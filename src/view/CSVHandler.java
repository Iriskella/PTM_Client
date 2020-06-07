package view;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CSVHandler {

    /**
     * Create file choose window and opening it
     *
     * @return file chosen ? the file : null
     */
    public static File LoadFile(Window window) {

        // Create a new file chooser window
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open csv file");
        fileChooser.setInitialDirectory(new File(".\\resources"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.
                ExtensionFilter("CSV Files", "*.csv"));

        // Open file chooser window and return selected file
        return fileChooser.showOpenDialog(window);
    }

    /**
     * @param csvFile CSV file
     * @return File data
     * @throws FileNotFoundException
     * @throws NoSuchElementException
     * @throws IllegalStateException
     */
    public static Pair<double[],int[][]> GetCsvData(File csvFile) throws FileNotFoundException,
            NoSuchElementException, IllegalStateException {

        if (csvFile == null) throw new FileNotFoundException();

        // Check file exception
        String fileName = csvFile.getName();
        int fileNameLength = fileName.length();
        if (!fileName.substring(fileNameLength - 3, fileNameLength).equals("csv")) throw new IllegalArgumentException();

        // Read from file
        double[] location = new double[3];
        Scanner scanner = new Scanner(csvFile);
        String[] line = scanner.next().split(",");
        location[0] = Double.parseDouble(line[0]);
        location[1] = Double.parseDouble(line[1]);
        line = scanner.next().split(",");
        location[2] = Double.parseDouble(line[0]);
        List<int[]> output = new ArrayList<>();
        while (scanner.hasNext())
            output.add(
                    Arrays.stream(scanner.next().split(",")).mapToInt(Integer::parseInt).toArray());
        scanner.close();

        // Return data
        return new Pair<double[],int[][]>(location, output.toArray(new int[0][0]));
    }


    // ---------------------------------------------  Class main ----------------------------------------------
    public static void main(String[] args) {
        try {
            File aFile = new File("./resources/honolulu_map.csv");
            int[][] fileData = CSVHandler.GetCsvData(aFile).getValue();
            int rowAmount = fileData.length, colAmount = fileData[0].length;
            for (int row = 0; row < rowAmount; row++)
                for (int col = 0; col < colAmount; col++) {
                    System.out.print(" " + fileData[row][col]);
                    if(col == colAmount - 1)
                        System.out.println("");
                }
        } catch (FileNotFoundException e) {
            System.out.println("File not exist");
        } catch (IllegalStateException e) {
            System.out.println("Wrong file extention");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
