package mainWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("window.fxml"));
        try {
            root = fxmlLoader.load();
            primaryStage.setScene(new Scene(root, 1150, 700));
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Mensch ärgere dich nicht!");


        if (controller != null) {
            controller.setupUI(primaryStage);
        } else {
            System.err.println("Loading main window gone horribly wrong -_-\nplease restart the Application");
        }

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
