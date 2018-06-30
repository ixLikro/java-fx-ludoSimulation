package mainWindow.statistic;

import mainWindow.Controller;
import mainWindow.model.Board;
import mainWindow.model.Color;
import mainWindow.model.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticHelper {
    private static StatisticHelper ourInstance = new StatisticHelper();

    public static StatisticHelper getInstance() {
        return ourInstance;
    }

    private List<GameStatistic> allStatistics;

    private StatisticHelper() {
        allStatistics = new ArrayList<>();
        allStatistics.add(new GameStatistic());
    }


    public void newGame(){
        allStatistics.add(new GameStatistic());
    }

    public GameStatistic getCurrentGameStat(){
        return allStatistics.get(allStatistics.size()-1);
    }

    public void reset(){
        allStatistics.clear();
        allStatistics.add(new GameStatistic());
    }
}
