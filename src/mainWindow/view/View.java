package mainWindow.view;

import mainWindow.Controller;
import mainWindow.model.Game;

public abstract class View {

    protected Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }

    abstract public void updateUI(Game game);
    abstract public void setupUI();
    abstract public void resetUI();
}
