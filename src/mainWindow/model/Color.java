package mainWindow.model;

import javafx.scene.paint.Paint;


public enum Color {

    GREEN(javafx.scene.paint.Color.GREEN),
    RED(javafx.scene.paint.Color.RED),
    BLUE(javafx.scene.paint.Color.BLUE),
    YELLOW(javafx.scene.paint.Color.YELLOW);

    private Paint paint;

    Color(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }


}
