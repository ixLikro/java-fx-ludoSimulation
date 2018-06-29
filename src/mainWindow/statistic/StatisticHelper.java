package mainWindow.statistic;

import mainWindow.model.Color;
import mainWindow.model.Game;

import java.util.HashMap;
import java.util.Map;

public class StatisticHelper {
    private static StatisticHelper ourInstance = new StatisticHelper();

    public static StatisticHelper getInstance() {
        return ourInstance;
    }

    private Map<Color, PlayerStatistic> allPlayerStatistics;

    private StatisticHelper() {
        allPlayerStatistics = new HashMap<>();
    }

    public void init(Game game){
        game.getAllPlayer().forEach(player -> allPlayerStatistics.put(player.getColor(), new PlayerStatistic()));
    }

    public PlayerStatistic getPlayerStatistic(Color color){
        return allPlayerStatistics.get(color);
    }

    public void reset(Game game){
        init(game);
    }



}
