package mainWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mainWindow.model.Game;
import mainWindow.view.BoardView;
import mainWindow.view.View;


import java.util.ArrayList;
import java.util.List;

public class Controller {

    //settings
    public static final int FIGURE_COUNT = 4;
    public static final int DICE_COUNT = 1;


    private List<View> allViews;
    private Stage primaryStage;

    private Game game;

    public Controller() {
        this.allViews = new ArrayList<>();

        allViews.add(new BoardView(this));

        game = new Game();
    }

    public void setupUI(Stage stage){
        primaryStage = stage;

        for (View v : allViews) {
            v.setupUI();
        }

        right_button_reset.setOnAction (e -> {
            resetALL();
            updateUI();
        });

        right_button_start.setOnAction(event -> {
            game.nextPlayerMove();
            updateUI();
        });
    }

    public void updateUI(){
        for (View v : allViews) {
            v.updateUI(game);
        }
    }

    public void resetALL(){
        for (View v : allViews) {
            v.resetUI();
        }

        game = new Game();
    }

    //gui elements
    @FXML public GridPane game_gridPane;
    @FXML public Button right_button_reset;
    @FXML public Button right_button_start;

}
