/**
 * Created by sean on 9/3/16.
 */
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Minesweeper extends Application {
    public static boolean takeTurn(Board b) {
        System.out.println(b);
        Scanner sc = new Scanner(System.in);
        System.out.println("Flag[0] or click[1]?: ");
        int turnChoice = sc.nextInt();
        System.out.print("\nEnter x: ");
        int x = sc.nextInt();
        System.out.print("\nEnter y: ");
        int y = sc.nextInt();
        if (turnChoice == 0) {
            b.flag(y, x);
            return true;
        } else if (turnChoice == 1) {
            return b.click(y, x);
        } else {
            System.out.println("Invalid turn");
            takeTurn(b);
        }
        return true;
    }
    @Override
    public void start(Stage primaryStage) {
        Board board = new Board(10, 8, 8, 2, 4);
        GridPane grid = new GridPane();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final int fi = i;
                final int fj = j;
                Button btn = new Button();
                btn.setText(board.getSquare(fi, fj).toString());
                btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                            if (event.isControlDown() || event.isSecondaryButtonDown()) {
                                board.flag(fi, fj);
                            } else {
                                board.click(fi, fj);
                            }
                            btn.setText(board.getSquare(fi, fj).toString());
                        }
                    }
                });
                grid.add(btn, j, i);
            }
        }
        Scene scene = new Scene(grid, 300, 250);
        primaryStage.setTitle("MineSweeper");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
