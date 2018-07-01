package mainWindow.statistic;

import mainWindow.model.Board;
import mainWindow.model.Color;

import java.util.ArrayList;
import java.util.List;

public class StatisticHelper {
    private static StatisticHelper ourInstance = new StatisticHelper();

    public static StatisticHelper getInstance() {
        return ourInstance;
    }

    private List<GameStatistic> allStatistics;

    private GameStatistic sumStatistic;

    private StatisticHelper() {
        allStatistics = new ArrayList<>();
        reset();
    }


    public void newGame(){
        calcSumStatistics();
        allStatistics.add(new GameStatistic());
    }

    public GameStatistic getCurrentGameStat(){
        return allStatistics.get(allStatistics.size()-1);
    }

    public int getGameCount(){
        return allStatistics.size()-1;
    }

    public GameStatistic getSumStatistic() {
        return sumStatistic;
    }

    public void reset(){
        allStatistics.clear();
        allStatistics.add(new GameStatistic());
        sumStatistic = new GameStatistic();
    }

    private void calcSumStatistics(){
        sumStatistic = new GameStatistic();

        //sum all fields
        for(int i = 0; i < Board.FIELD_COUNT; i++){
            for(GameStatistic gameStatistic : allStatistics){
                for(Color color : Color.values()){
                    sumStatistic.getFieldStatistic(i).increment(color, gameStatistic.getFieldStatistic(i)
                            .getOneStatItem(color).getValueAsInteger());
                }
            }
        }

        //sum player stats
        for(Color color : Color.values()){
            for(GameStatistic gameStatistic : allStatistics){
                sumStatistic.getPlayerStatistic(color).getTurnCount().increment(gameStatistic.getPlayerStatistic(color).getTurnCount().getValueAsInteger());
                sumStatistic.getPlayerStatistic(color).getHasBumped().increment(gameStatistic.getPlayerStatistic(color).getHasBumped().getValueAsInteger());
                sumStatistic.getPlayerStatistic(color).getWasBumped().increment(gameStatistic.getPlayerStatistic(color).getWasBumped().getValueAsInteger());

                for(int i = 1; i < 7; i++){
                    sumStatistic.getPlayerStatistic(color).incrementDiceRoll(i, gameStatistic.getPlayerStatistic(color).getOneDiceRollCount(i));
                }
            }
        }
    }
}
