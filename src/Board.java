/**
 * Created by sean on 9/3/16.
 */
import java.util.Random;
public class Board {
    private int mines;
    private int flags;
    private int width;
    private int height;
    private Square[][] grid;

    public Board(int mines, int width, int height, int startW, int startH) {
        this.mines = mines;
        this.width = width;
        this.height = height;
        grid = new Square[height][width];
        Random rand = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Square(i, j);
            }
        }
        for (int i = 0; i < mines; i++) {
            int nextWidth = rand.nextInt(width);
            int nextHeight = rand.nextInt(height);
            if ((nextWidth == startW && nextHeight == startH) ||
            !grid[nextHeight][nextWidth].addMine()) {
                i--;
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j].setAdjacent(calcAdjacent(i, j));
            }
        }
        click(startH, startW);
    }
    public Square getSquare(int h, int w) {
        return grid[h][w];
    }
    public boolean flag(int h, int w) {
        Square s = grid[h][w];
        if (s.isClicked()) {
            return false;
        } else {
            s.flag();
            return true;
        }
    }
    public boolean click(int h, int w) {
        Square s = grid[h][w];
        if (s.isFlag() || s.isClicked()) {
            return true;
        } else if (s.isMine()) {
            s.setClicked();
            return false;
        } else {
            s.setClicked();
            return true;
        }
    }
    private int calcAdjacent(int h, int w) {
        int total = 0;
        for (int i = h - 1; i < h + 2; i++) {
            for (int j = w - 1; j < w + 2; j++) {
                if (i >= 0 && j >= 0 && i < height && j < width) {
                    total += grid[i][j].isMine() ? 1 : 0;
                }
            }
        }
        return total;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(grid[i][j].toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
