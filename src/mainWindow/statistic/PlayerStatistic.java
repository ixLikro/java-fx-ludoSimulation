package mainWindow.statistic;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatistic {

    StatisticItemInt turnCount, diceRollCount, countNotMove, wasBumped, hasBumped;

    public PlayerStatistic() {
        turnCount = new StatisticItemInt("Anzahl Züge: ");
        diceRollCount = new StatisticItemInt("Anzahl würfeln: ");
        countNotMove = new StatisticItemInt("durfte keine Figur weiter setzen: ");
        wasBumped = new StatisticItemInt("wurde rausgeworfen: ");
        hasBumped = new StatisticItemInt("hat rausgeworfen: ");
    }

    private List<StatisticItem> getValuesAsList(){
        List<StatisticItem> ret = new ArrayList<>();
        ret.add(turnCount);
        ret.add(diceRollCount);
        ret.add(wasBumped);
        ret.add(hasBumped);
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        List<StatisticItem> valuesAsList = getValuesAsList();

        for (int i = 0; i < valuesAsList.size(); i++) {
            ret.append(valuesAsList.get(i).toString());
            if(i != valuesAsList.size()-1){
                ret.append("\n");
            }
        }

        return ret.toString();
    }

    public StatisticItemInt getTurnCount() {
        return turnCount;
    }

    public StatisticItemInt getDiceRollCount() {
        return diceRollCount;
    }

    public StatisticItemInt getWasBumped() {
        return wasBumped;
    }

    public StatisticItemInt getHasBumped() {
        return hasBumped;
    }

    public StatisticItemInt getCountNotMove() {
        return countNotMove;
    }
}
