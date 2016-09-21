/**
 * Created by sean on 9/3/16.
 */
import javafx.application.Application;
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
import javafx.scene.text.Font;
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
        flagText.setText("\u2690\n" + board.getFlags() + "/" + board.getMines());
    }
    private void endGame(boolean won, int mines, int height, int width, Stage stage, int prefHeight) {
        ButtonType newGameButton = new ButtonType("New Game", ButtonBar.ButtonData.OK_DONE);
        Dialog<ButtonType> over = new Dialog<>();
        over.setContentText(won ? "You win!" : "You lose");
        over.setTitle("Game Over");
        over.getDialogPane().getButtonTypes().add(newGameButton);
        over.showAndWait().ifPresent(response -> {
            if (response == newGameButton) {
                playScene(mines, height, width, stage, prefHeight);
            }
        });

    }
    private void initialize(GridPane grid, Board board, Text flagText, Stage stage, int prefHeight) {
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                final int fi = i;
                final int fj = j;
                Button btn = new Button();
                btn.setText(board.getSquare(fi, fj).toString());
                btn.setPrefHeight(prefHeight);
                btn.setPrefWidth(prefHeight);
                btn.setMinHeight(30);
                btn.setMinWidth(30);
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
                                    endGame(false, board.getMines(), board.getHeight(), board.getWidth(), stage, prefHeight);
                                } else if (board.isWon()) {
                                    updateBoard(grid, board, flagText);
                                    endGame(true, board.getMines(), board.getHeight(), board.getWidth(), stage, prefHeight);
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
    private void showStage(Stage stage) {
        stage.show();
    }
    private void selectScene(Stage stage) {
        GridPane grid = new GridPane();
        grid.setPrefWidth(400);
        grid.setPrefHeight(400);
        Button easy = new Button("Easy");
        Button med = new Button("Medium");
        Button hard = new Button("Hard");
        Button cust = new Button("Custom");
        easy.setPrefWidth(grid.getPrefWidth());
        med.setPrefWidth(grid.getPrefWidth());
        hard.setPrefWidth(grid.getPrefWidth());
        cust.setPrefWidth(grid.getPrefWidth());
        easy.setPrefHeight(grid.getPrefHeight());
        med.setPrefHeight(grid.getPrefHeight());
        hard.setPrefHeight(grid.getPrefHeight());
        cust.setPrefHeight(grid.getPrefHeight());
        easy.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    playScene(10, 8, 8, stage, 75);
                }
            }
        });
        med.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    playScene(40, 16, 16, stage, 50);
                }
            }
        });
        hard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    playScene(99, 16, 30, stage, 40);
                }
            }
        });
        cust.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    playScene(10, 8, 8, stage, 100);
                }
            }
        });
        grid.add(easy, 0, 0);
        grid.add(med, 1, 0);
        grid.add(hard, 0, 1);
        grid.add(cust, 1, 1);
        Scene scene = new Scene(grid);
        stage.setScene(scene);
    }
    private void playScene(int mines, int height, int width, Stage stage, int prefHeight) {
        HBox hbox = new HBox();
        GridPane grid = new GridPane();
        Text flagText = new Text("\u2690\n 0/" + mines);
        flagText.setFont(new Font(20));
        initialize(grid, new Board(mines, width, height), flagText, stage, prefHeight);
        VBox sideBar = new VBox();
        Button startOver = new Button("Start Over");
        Button changeDifficulty = new Button("Change Difficulty");
        startOver.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    playScene(mines, height, width, stage, prefHeight);
                }
            }
        });
        changeDifficulty.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    selectScene(stage);
                }
            }
        });
        sideBar.getChildren().addAll(flagText, startOver, changeDifficulty);
        hbox.getChildren().addAll(grid, sideBar);
        Scene scene = new Scene(hbox);
        stage.setScene(scene);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MineSweeper");
        selectScene(primaryStage);
        showStage(primaryStage);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
