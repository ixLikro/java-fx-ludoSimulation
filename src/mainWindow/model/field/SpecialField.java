package mainWindow.model.field;

import mainWindow.model.Color;

public class SpecialField extends Field {

    private Color color;

    public SpecialField(int index, Color color) {
        super(index);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
