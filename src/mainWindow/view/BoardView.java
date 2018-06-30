package mainWindow.view;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import mainWindow.Controller;
import mainWindow.model.Game;
import mainWindow.model.field.FinishField;
import mainWindow.model.field.StartField;
import mainWindow.statistic.StatisticHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardView extends View {

    private final Paint NORMAL_DEFAULT_PAINT = Color.GRAY.brighter();
    private final Paint SPECIAL_DEFAULT_PAINT = Color.WHITE;


    private List<Circle> allFieldShapes;

    private Map<mainWindow.model.Color, List<Circle>> allFinnishShapes;
    private Map<mainWindow.model.Color, List<Circle>> allStartShapes;
    private Map<mainWindow.model.Color, Label> allDiceLabels;

    public BoardView(Controller controller) {
        super(controller);

        allFieldShapes = new ArrayList<>();
        allFinnishShapes = new HashMap<>();
        allStartShapes = new HashMap<>();
        allDiceLabels = new HashMap<>();
    }


    @Override
    public void setupUI() {

        allFieldShapes.clear();
        for (int i = 0; i < 40; i++){
            Circle temp = createCircle(i);
            int[] pos = fieldIndexToCell(i);
            controller.game_gridPane.add(temp, pos[0], pos[1]);
            allFieldShapes.add(temp);
        }

        allFinnishShapes.clear();
        for(mainWindow.model.Color color : mainWindow.model.Color.values()){
            allFinnishShapes.put(color, new ArrayList<>());
            for (int i = 0; i < Controller.FIGURE_COUNT; i++) {
                Circle temp = createSpecialCircle(color);
                int[] pos = colorANDFinnishIndexToCell(color , i);
                controller.game_gridPane.add(temp, pos[0], pos[1]);
                GridPane.setHalignment(temp, HPos.CENTER);
                GridPane.setValignment(temp, VPos.CENTER);
                allFinnishShapes.get(color).add(temp);
            }
        }

        allStartShapes.clear();
        for(mainWindow.model.Color color : mainWindow.model.Color.values()){
            allStartShapes.put(color, new ArrayList<>());
            for (int i = 0; i < Controller.FIGURE_COUNT; i++) {
                Circle temp = createSpecialCircle(color);
                int[] pos = colorANDStartIndexToCell(color , i);
                controller.game_gridPane.add(temp, pos[0], pos[1]);
                GridPane.setHalignment(temp, HPos.CENTER);
                GridPane.setValignment(temp, VPos.CENTER);
                allStartShapes.get(color).add(temp);
            }
        }

        allDiceLabels.clear();
        for(mainWindow.model.Color color : mainWindow.model.Color.values()){
            Label temp = createNewDiceLabel(color);
            int[] pos = colorToDiceCell(color);
            controller.game_gridPane.add(temp, pos[0], pos[1]);
            GridPane.setHalignment(temp, HPos.CENTER);
            GridPane.setValignment(temp, VPos.CENTER);
            allDiceLabels.put(color, temp);
        }
    }

    @Override
    public void updateUI(Game game) {
        resetUI();
        game.getAllPlayer().forEach(player -> {

            allDiceLabels.get(player.getColor()).setText(player.getLastDiceRoll()+"");

            player.getAllFigures().forEach(figure -> {
                if (figure.getIsOn() instanceof FinishField) {
                    allFinnishShapes.get(player.getColor()).get(figure.getIsOn().getIndex()).setFill(player.getColor().getPaint());
                } else if (figure.getIsOn() instanceof StartField) {
                    allStartShapes.get(player.getColor()).get(figure.getIsOn().getIndex()).setFill(player.getColor().getPaint());
                } else {
                    allFieldShapes.get(figure.getIsOn().getIndex()).setFill(player.getColor().getPaint());
                }
            });
        });


    }


    @Override
    public void resetUI() {
        allFieldShapes.forEach(circle -> circle.setFill(NORMAL_DEFAULT_PAINT));
        allStartShapes.forEach((color, circles) -> circles.forEach(circle -> circle.setFill(SPECIAL_DEFAULT_PAINT)));
        allFinnishShapes.forEach((color, circles) -> circles.forEach(circle -> circle.setFill(SPECIAL_DEFAULT_PAINT)));
    }

    private int[] fieldIndexToCell(int fieldIndex){
        int[] ret = new int[2];

        if(fieldIndex >= 40){
            fieldIndex = fieldIndex % 40;
        }

        switch (fieldIndex){
            //1st quarter
            case 0: ret[0] = 6; ret[1] = 0; break;
            case 1: ret[0] = 6; ret[1] = 1; break;
            case 2: ret[0] = 6; ret[1] = 2; break;
            case 3: ret[0] = 6; ret[1] = 3; break;
            case 4: ret[0] = 6; ret[1] = 4; break;
            case 5: ret[0] = 7; ret[1] = 4; break;
            case 6: ret[0] = 8; ret[1] = 4; break;
            case 7: ret[0] = 9; ret[1] = 4; break;
            case 8: ret[0] = 10; ret[1] = 4; break;
            case 9: ret[0] = 10; ret[1] = 5; break;

            //2nd quarter
            case 10: ret[0] = 10; ret[1] = 6; break;
            case 11: ret[0] = 9; ret[1] = 6; break;
            case 12: ret[0] = 8; ret[1] = 6; break;
            case 13: ret[0] = 7; ret[1] = 6; break;
            case 14: ret[0] = 6; ret[1] = 6; break;
            case 15: ret[0] = 6; ret[1] = 7; break;
            case 16: ret[0] = 6; ret[1] = 8; break;
            case 17: ret[0] = 6; ret[1] = 9; break;
            case 18: ret[0] = 6; ret[1] = 10; break;
            case 19: ret[0] = 5; ret[1] = 10; break;

            //3rd quarter
            case 20: ret[0] = 4; ret[1] = 10; break;
            case 21: ret[0] = 4; ret[1] = 9; break;
            case 22: ret[0] = 4; ret[1] = 8; break;
            case 23: ret[0] = 4; ret[1] = 7; break;
            case 24: ret[0] = 4; ret[1] = 6; break;
            case 25: ret[0] = 3; ret[1] = 6; break;
            case 26: ret[0] = 2; ret[1] = 6; break;
            case 27: ret[0] = 1; ret[1] = 6; break;
            case 28: ret[0] = 0; ret[1] = 6; break;
            case 29: ret[0] = 0; ret[1] = 5; break;

            //4rd quarter
            case 30: ret[0] = 0; ret[1] = 4; break;
            case 31: ret[0] = 1; ret[1] = 4; break;
            case 32: ret[0] = 2; ret[1] = 4; break;
            case 33: ret[0] = 3; ret[1] = 4; break;
            case 34: ret[0] = 4; ret[1] = 4; break;
            case 35: ret[0] = 4; ret[1] = 3; break;
            case 36: ret[0] = 4; ret[1] = 2; break;
            case 37: ret[0] = 4; ret[1] = 1; break;
            case 38: ret[0] = 4; ret[1] = 0; break;
            case 39: ret[0] = 5; ret[1] = 0; break;
        }

        return ret;
    }

    private int[] colorANDFinnishIndexToCell(mainWindow.model.Color color, int finnishIndex){
        int[] ret = new int[2];

        if(finnishIndex >= Controller.FIGURE_COUNT){
            finnishIndex = finnishIndex % Controller.FIGURE_COUNT;
        }

        switch (color){
            case GREEN:
                ret[0] = 5;
                ret[1] = 1 + finnishIndex;
                break;
            case RED:
                ret[0] = 9 - finnishIndex;
                ret[1] = 5;
                break;
            case BLUE:
                ret[0] = 5;
                ret[1] = 9 - finnishIndex;
                break;
            case YELLOW:
                ret[0] = 1 + finnishIndex;
                ret[1] = 5;
                break;
        }

        return ret;
    }

    private int[] colorANDStartIndexToCell(mainWindow.model.Color color, int startIndex){
        int[] ret = new int[2];

        if(startIndex >= Controller.FIGURE_COUNT){
            startIndex = startIndex % Controller.FIGURE_COUNT;
        }

        switch (color){
            case GREEN:
                ret[0] = 9;
                ret[1] = 0;
                break;
            case RED:
                ret[0] = 9;
                ret[1] = 9;
                break;
            case BLUE:
                ret[0] = 0;
                ret[1] = 9;
                break;
            case YELLOW:
                ret[0] = 0;
                ret[1] = 0;
                break;
        }

        ret[0] = ret[0] + (startIndex / 2);
        ret[1] = ret[1] + (startIndex % 2 == 0 ? 0 : 1);

        return ret;
    }

    private int[] colorToDiceCell(mainWindow.model.Color color){
        int[] ret = new int[2];

        switch (color){
            case GREEN:
                ret[0] = 7;
                ret[1] = 0;
                break;
            case RED:
                ret[0] = 10;
                ret[1] = 7;
                break;
            case BLUE:
                ret[0] = 3;
                ret[1] = 10;
                break;
            case YELLOW:
                ret[0] = 0;
                ret[1] = 3;
                break;
        }
        return ret;
    }

    private Circle createCircle(int index){
        Circle ret = new Circle();
        ret.setRadius(30);
        ret.setFill(NORMAL_DEFAULT_PAINT);
        ret.setStroke(Color.BLACK);
        ret.setStrokeWidth(3d);

        ret.setOnMouseEntered(event -> {
            Tooltip tip = new Tooltip(StatisticHelper.getInstance().getCurrentGameStat()
                    .getFieldStatistic(index).toString());
            tip.setShowDelay(Duration.millis(50));
            tip.setShowDuration(Duration.INDEFINITE);
            tip.setFont(new Font(tip.getFont().getFamily(),14));
            Tooltip.install(ret, tip);
        });

        return ret;
    }

    private Circle createSpecialCircle(mainWindow.model.Color color){
        Circle ret = new Circle();
        ret.setRadius(20);
        ret.setFill(Color.WHITE);
        ret.setStroke(color.getPaint());
        ret.setStrokeWidth(3d);

        ret.setOnMouseEntered(event -> {
            Tooltip tip = new Tooltip(StatisticHelper.getInstance().getCurrentGameStat()
                    .getPlayerStatistic(color).toString());
            tip.setShowDelay(Duration.millis(50));
            tip.setShowDuration(Duration.INDEFINITE);
            tip.setFont(new Font(tip.getFont().getFamily(),14));
            Tooltip.install(ret, tip);
        });

        return ret;
    }

    private Label createNewDiceLabel(mainWindow.model.Color color){
        Label ret = new Label();
        ret.setText("0");
        ret.setTextFill(color.getPaint());
        ret.setFont(new Font(ret.getFont().getFamily(), 30));
        return ret;
    }


}
