package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class extends canvas and draws height map represented by height matrix
 */
public class HeightMapDisplayer extends Canvas {

    public static int CubeLength = 2; // Represent cube side length
    private final int MaxHeight = 800; // Estimated height max value
    private final int MinHeight = 0; // Estimated height min value
    private final GraphicsContext graphicsContext = super.getGraphicsContext2D(); //Drawer

    private int[][] _heightMatrix;
    private int _rowAmount, _colAmount;

    // Constructor
    public HeightMapDisplayer() {
        _heightMatrix = null;
        _rowAmount = -1;
        _colAmount = -1;
    }

    // Getters
    public int get_rowAmount() {
        return _rowAmount;
    }

    public int get_colAmount() {
        return _colAmount;
    }


   /* public void zoom(double value) {
        CubeLength += value;
        super.setWidth(_colAmount * CubeLength);
        super.setHeight(_rowAmount * CubeLength);
        this.drawMap();
    }*/

    /**
     * Updates height matrix and canvas size
     *
     * @param heightMatrix integers matrix representing height map
     */
    public void set_heightMatrix(int[][] heightMatrix) {

        // Update data
        _heightMatrix = heightMatrix;
        _rowAmount = heightMatrix.length;
        _colAmount = heightMatrix[0].length;

        // Set canvas width and height
        super.setWidth(_colAmount * CubeLength);
        super.setHeight(_rowAmount * CubeLength);

        // Draw the new matrix
        this.drawMap();
    }

    /**
     * Draw the height map values while referencing to the height color.
     * ---> red = closer to MinHeight
     * ---> green = closer to MaxHeight
     */
    private void drawMap() {
        if (_heightMatrix == null) return;

        graphicsContext.clearRect(0, 0, super.getWidth(), super.getHeight());

        for (int row = 0; row < _rowAmount; row++)
            for (int col = 0; col < _colAmount; col++) {

                int value = _heightMatrix[row][col];
                value = Math.max(MinHeight, Math.min(MaxHeight, value));

                // Set color for each value
                int red = (MaxHeight - value) > 200 ? 255 : 70, blue = 0,
                        green = (int) ((double) value / ((double) MaxHeight / 255.0));
                graphicsContext.setFill(Color.rgb(red, green, blue));

                // Draw cube
                int x = col * CubeLength, y = row * CubeLength;
                graphicsContext.fillRect(x, y, CubeLength, CubeLength);

                // place value
                //graphicsContext.setFill(Color.WHITE);
                //graphicsContext.fillText(Integer.toString(value), x + 10, y + 10, CubeLength / 2);
            }
    }
}
