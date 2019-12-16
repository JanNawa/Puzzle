package assignment_4;

/**
 * For indicate the starting box and direction in the puzzle
 * 
 * @author Jan
 */
public class PuzzleBox {
    private int row;
    private int column;
    private String direction;

    public PuzzleBox(int row, int column, String direction) {
        this.row = row;
        this.column = column;
        this.direction = direction;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getDirection() {
        return direction;
    }
}