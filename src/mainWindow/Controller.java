package mainWindow;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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


    public Controller() {
        this.allViews = new ArrayList<>();

        allViews.add(new BoardView(this));
    }

    public void setupUI(Stage stage){
        primaryStage = stage;

        for (View v : allViews) {
            v.setupUI();
        }
    }

    //gui elements
    @FXML public GridPane game_gridPane;

}
