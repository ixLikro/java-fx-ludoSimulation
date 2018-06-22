package mainWindow.model;

import javafx.scene.paint.Paint;


public enum Color {

    GREEN(javafx.scene.paint.Color.GREEN.darker()),
    RED(javafx.scene.paint.Color.RED.darker()),
    BLUE(javafx.scene.paint.Color.BLUE.darker()),
    YELLOW(javafx.scene.paint.Color.YELLOW.darker());

    private Paint paint;

    Color(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }


}
