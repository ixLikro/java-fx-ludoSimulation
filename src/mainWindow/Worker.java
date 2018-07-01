package mainWindow;

import javafx.application.Platform;

public class Worker extends Thread{

    private Controller controller;

    public Worker(Controller controller) {
        this.controller = controller;

    }

    @Override
    synchronized public void run() {

        while(!Thread.interrupted()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }

            if (controller.getGame().nextPlayerMove()) {
                Platform.runLater(() -> controller.onGameFinished());
                return;
            }

            Platform.runLater(() -> controller.updateUI());
        }
    }
}
