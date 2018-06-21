package mainWindow.view;

import mainWindow.Controller;

public abstract class View {

    protected Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }

    abstract public void updateUI();
    abstract public void setupUI();
    abstract public void resetUI();
}
