package mainWindow.model;

import mainWindow.Controller;
import mainWindow.model.field.Field;
import mainWindow.model.field.StartField;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Figure> allFigures;
    private Color color;

    public Player(Color color) {
        this.color = color;

        allFigures = new ArrayList<>();
    }

    public void setup(List<StartField> startFields){
        allFigures.clear();
        for (int i = 0;  i < Controller.FIGURE_COUNT ; i++) {
            allFigures.add(new Figure(startFields.get(i)));
        }
    }

    public boolean isOnField(Field field){
        boolean ret = false;

        for (Figure figure : allFigures) {
            if (figure.getIsOn().equals(field)) {
                ret = true;
            }
        }

        return ret;
    }

    public void performMove(int diceRoll, Board board){

    }

    public Color getColor() {
        return color;
    }
}
