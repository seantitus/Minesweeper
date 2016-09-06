/**
 * Created by sean on 9/3/16.
 */
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Minesweeper extends Application {
    public void updateBoard(Board board, GridPane grid) {
        for (Node node : grid.getChildren()) {
            Button btn = (Button) node;
            Square sq = board.getSquare(grid.getRowIndex(node), grid.getColumnIndex(node));
            btn.setText(sq.toString());
            if (sq.isClicked()) {
                btn.setStyle("-fx-base: #d8d8d8;");
            }
        }
    }
    private void initialize(GridPane grid, Board board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final int fi = i;
                final int fj = j;
                Button btn = new Button();
                btn.setText(board.getSquare(fi, fj).toString());
                btn.setMinHeight(50);
                btn.setMinWidth(50);
                btn.setStyle("-fx-base: #b9b9b9;");
                btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                            if (event.isControlDown() || event.isSecondaryButtonDown()) {
                                board.flag(fi, fj);
                            } else {
                                if (!board.click(fi, fj)) {
                                    System.out.println("u lose");
                                }
                            }
                            updateBoard(board, grid);
                        }
                    }
                });
                grid.add(btn, j, i);
            }
        }
    }
    @Override
    public void start(Stage primaryStage) {
        HBox hbox = new HBox();
        GridPane grid = new GridPane();
        initialize(grid, new Board(10, 8, 8));
        VBox sideBar = new VBox();
        Button startOver = new Button();
        startOver.setText("Start Over");
        startOver.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    initialize(grid, new Board(10, 8, 8));
                }
            }
        });
        sideBar.getChildren().addAll(startOver);
        hbox.getChildren().addAll(grid, sideBar);
        Scene scene = new Scene(hbox);
        primaryStage.setTitle("MineSweeper");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
