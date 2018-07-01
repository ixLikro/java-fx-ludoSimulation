package mainWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mainWindow.model.Game;
import mainWindow.statistic.StatisticHelper;
import mainWindow.view.BoardView;
import mainWindow.view.View;


import java.util.ArrayList;
import java.util.List;

public class Controller {

    //settings
    public static final int FIGURE_COUNT = 4;

    private List<View> allViews;
    private Thread worker;
    private Thread timer;

    private boolean autoRunning;

    private Game game;

    public Controller() {
        this.allViews = new ArrayList<>();

        allViews.add(new BoardView(this));

        game = new Game();

        newWorker();
    }

    private void newWorker(){
        worker = new Worker(this);
    }


    public void setup(Stage stage){
        //set up all views
        for (View v : allViews) {
            v.setupUI();
        }

        right_button_reset.setOnAction (e -> {

            timer.interrupt();

            resetALL();
            updateUI();
        });

        right_button_start.setOnAction(event -> {

            if(!timer.isAlive()){
                timer.start();
                right_button_start.setText("Pause");
            }

            //after the first click override it self to change behavior
            right_button_start.setOnAction(event1 -> {
                if(!worker.isAlive()){
                    newGame();
                }else {
                    if(!timer.isAlive()){
                        right_button_start.setText("Pause");
                        timer = new Timer(worker, right_slider);
                        timer.start();
                    }else {
                        right_button_start.setText("Fortsetzen");
                        timer.interrupt();
                    }
                }
            });
        });

        //update slider text
        right_slider.valueProperty().addListener((observable, oldValue, newValue) ->
                right_slider_label.setText("Geschwindigkeit: "+newValue.intValue()+" (Züge pro Sekunde)"));


        //setup worker and timer thread
        worker.start();
        timer = new Timer(worker, right_slider);
    }


    public void shutDown(){
        timer.interrupt();
        worker.interrupt();
    }

    public void updateUI(){
        for (View v : allViews) {
            v.updateUI(game);
        }
    }

    public void onGameFinished(){
        StatisticHelper.getInstance().newGame();

        if(right_checkbox_autoRestart.isSelected()){
            newGame();
        }

        right_button_start.setText("Neues Spiel starten");
    }

    public void newGame(){

        game = new Game();
        for (View v : allViews) {
            v.resetUI();
        }

        newWorker();
        worker.start();
        timer = new Timer(worker, right_slider);
        timer.start();

        right_button_start.setText("Pause");
    }

    public void resetALL(){
        for (View v : allViews) {
            v.resetUI();
        }

        game = new Game();

        StatisticHelper.getInstance().reset();
    }

    public Game getGame() {
        return game;
    }

    //gui elements
    @FXML public GridPane game_gridPane;
    @FXML public Button right_button_reset;
    @FXML public Button right_button_start;
    @FXML public Slider right_slider;
    @FXML public Label right_slider_label;
    @FXML public CheckBox right_checkbox_autoRestart;

}
