/**
 * Created by sean on 9/3/16.
 */
public class Square {
    private int x;
    private int y; //origin is top left
    private int adjacent;
    private boolean clicked;
    private boolean mine;
    private boolean flag;
    public Square(int x, int y) {
        this.x = x;
        this.y = y;
        mine = false;
        flag = false;
        adjacent = 0;
        clicked = false;
    }
    public boolean addMine() {
        if (mine) {
            return false;
        }
        mine = true;
        return true;
    }
    public boolean isMine() {
        return mine;
    }
    public boolean isClicked() {
        return clicked;
    }
    public boolean isFlag() {
        return flag;
    }
    public int getAdjacent() {
        return adjacent;
    }
    public void setAdjacent(int adjacent) {
        this.adjacent = adjacent;
    }
    public void setClicked() {
        clicked = true;
    }
    public void flag() {
        flag = !flag;
    }
    @Override
    public String toString() {
        if (flag) {
            return "F";
        } else if (clicked) {
            return mine ? "X" : String.valueOf(adjacent);
        } else {
            return " ";
        }
    }
}
