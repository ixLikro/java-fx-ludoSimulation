package mainWindow.model;

import mainWindow.model.field.Field;

import java.util.Objects;

public class Figure {

    private Field isOn;

    public Figure(Field isOn) {
        this.isOn = isOn;
    }


    public Field getIsOn() {
        return isOn;
    }

    public void setIsOn(Field isOn) {
        this.isOn = isOn;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Figure figure = (Figure) o;
        return Objects.equals(getIsOn(), figure.getIsOn());
    }


    @Override
    public String toString() {
        return "Figure{" +
                "isOn=" + isOn +
                '}';
    }
}
