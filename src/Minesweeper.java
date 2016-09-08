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
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Minesweeper extends Application {
    private void updateBoard(GridPane grid, Board board, Text flagText) {
        for (Node node : grid.getChildren()) {
            Button btn = (Button) node;
            Square sq = board.getSquare(grid.getRowIndex(node), grid.getColumnIndex(node));
            btn.setText(sq.toString());
            if (sq.isClicked()) {
                btn.setStyle("-fx-base: #d8d8d8;");
            }
        }
        flagText.setText("Flags: " + board.getFlags() + "/" + board.getMines());
    }
    private void endGame(boolean won, GridPane grid, Board board, Text flagText) {
        ButtonType newGameButton = new ButtonType("New Game", ButtonBar.ButtonData.OK_DONE);
        Dialog<ButtonType> over = new Dialog<>();
        over.setContentText(won ? "You win!" : "You lose");
        over.setTitle("Game Over");
        over.getDialogPane().getButtonTypes().add(newGameButton);
        over.showAndWait().ifPresent(response -> {
            if (response == newGameButton) {
                initialize(grid, board, flagText);
            }
        });

    }
    private void initialize(GridPane grid, Board board, Text flagText) {
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
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
                            if (event.isControlDown() || event.getButton() == MouseButton.SECONDARY) {
                                board.flag(fi, fj);
                                updateBoard(grid, board, flagText);
                            } else {
                                if (!board.click(fi, fj)) {
                                    updateBoard(grid, board, flagText);
                                    Board temp = new Board(board.getMines(), board.getWidth(),
                                            board.getHeight());
                                    endGame(false, grid, temp, flagText);
                                    updateBoard(grid, temp, flagText);
                                } else if (board.isWon()) {
                                    updateBoard(grid, board, flagText);
                                    Board temp = new Board(board.getMines(), board.getWidth(),
                                            board.getHeight());
                                    endGame(true, grid, temp, flagText);
                                    updateBoard(grid, temp, flagText);
                                } else {
                                    updateBoard(grid, board, flagText);
                                }
                            }
                        }
                    }
                });
                grid.add(btn, j, i);
                updateBoard(grid, board, flagText);
            }
        }
    }
    @Override
    public void start(Stage primaryStage) {
        int mines = 10;
        int height = 8;
        int width = 8;
        HBox hbox = new HBox();
        GridPane grid = new GridPane();
        Text flagText = new Text("Flags: 0/" + mines);
        initialize(grid, new Board(mines, height, width), flagText);
        VBox sideBar = new VBox();
        Button startOver = new Button();
        startOver.setText("Start Over");
        startOver.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    initialize(grid, new Board(mines, height, width), flagText);
                }
            }
        });
        sideBar.getChildren().addAll(flagText, startOver);
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
