package mainWindow.statistic;

import mainWindow.model.Board;
import mainWindow.model.Color;

import java.util.HashMap;
import java.util.Map;

public class GameStatistic {

    private Map<Color, PlayerStatistic> allPlayerStatistics;
    private Map<Integer, FieldStatistic> allFieldStatistics;

    public GameStatistic(){
        allPlayerStatistics = new HashMap<>();
        allFieldStatistics = new HashMap<>();

        for(Color color : Color.values()){
            allPlayerStatistics.put(color, new PlayerStatistic());
        }

        for(int i = 0; i< Board.FIELD_COUNT; i++){
            allFieldStatistics.put(i, new FieldStatistic());
        }
    }

    public PlayerStatistic getPlayerStatistic(Color color){
        return allPlayerStatistics.get(color);
    }

    public FieldStatistic getFieldStatistic(int fieldIndex){
        return allFieldStatistics.get(fieldIndex);
    }
}
