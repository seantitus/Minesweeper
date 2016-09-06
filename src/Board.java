/**
 * Created by sean on 9/3/16.
 */
import java.util.Random;
public class Board {
    private boolean firstClick;
    private int mines;
    private int flags;
    private int width;
    private int height;
    private Square[][] grid;

    public Board(int mines, int width, int height) {
        this.mines = mines;
        this.width = width;
        this.height = height;
        firstClick = true;
        grid = new Square[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Square(i, j);
            }
        }
    }
    public void populateMines(int startH, int startW) {
        Random rand = new Random();
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
    }
    public Square getSquare(int h, int w) {
        return grid[h][w];
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
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
    public boolean clickAdjacent(int h, int w) {
        boolean mines = true;
        for (int i = h - 1; i < h + 2; i++) {
            for (int j = w - 1; j < w + 2; j++) {
                if (i >= 0 && j >= 0 && i < height && j < width && !(i == h && j == w)) {
                    Square s = grid[i][j];
                    if (!s.isFlag() && !s.isClicked()) {
                        s.setClicked();
                        if (s.getAdjacent() == 0) {
                            clickAdjacent(i, j);
                        }
                    }
                    else if (!s.isFlag() && s.isMine()) {
                        s.setClicked();
                        mines = false;
                    }
                }
            }
        }
        return mines;
    }
    public boolean click(int h, int w) {
        if (firstClick) {
            populateMines(h, w);
            firstClick = false;
        }
        Square s = grid[h][w];
        if (s.isFlag()) {
            return true;
        } else if (s.isClicked()) {
            int flags = 0;
            for (int i = h - 1; i < h + 2; i++) {
                for (int j = w - 1; j < w + 2; j++) {
                    if (i >= 0 && j >= 0 && i < height && j < width) {
                        flags += grid[i][j].isFlag() ? 1 : 0;
                    }
                }
            }
            if (flags == s.getAdjacent()) {
                return clickAdjacent(h, w);
            }
            return true;
        } else if (s.isMine()) {
            s.setClicked();
            return false;
        } else {
            s.setClicked();
            if (s.getAdjacent() == 0) {
                clickAdjacent(h, w);
            }
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
