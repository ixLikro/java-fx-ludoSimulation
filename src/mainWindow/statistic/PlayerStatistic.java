package mainWindow.statistic;

import java.util.*;

public class PlayerStatistic {

    private StatisticItemInt turnCount, wasBumped, hasBumped;
    private SupplierStatistic diceRollCount;
    private Map<Integer, Integer> allDiceRolls;

    public PlayerStatistic() {
        allDiceRolls = new HashMap<>();
        for(int i = 1; i < 7; i++){
            allDiceRolls.put(i,0);
        }

        turnCount = new StatisticItemInt("Anzahl Züge: ");
        diceRollCount = new SupplierStatistic("Anzahl würfeln: ", this::calculateDiceRollCount);
        wasBumped = new StatisticItemInt("Wurde rausgeworfen: ");
        hasBumped = new StatisticItemInt("Hat rausgeworfen: ");
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
            ret.append("\n");
        }

        ret.append("\nWürfel Ergebnisse:\n");

        for (Map.Entry<Integer, Integer> entry : allDiceRolls.entrySet()) {
            ret.append("Die ")
                    .append(entry.getKey())
                    .append(" wurde ")
                    .append(entry.getValue())
                    .append(" mal gewürfelt")
                    .append("\n");
        }

        return ret.toString();
    }

    public void incrementDiceRoll(int diceRoll){
        allDiceRolls.put(diceRoll, allDiceRolls.get(diceRoll)+1);
    }

    private int calculateDiceRollCount(){
        int ret = 0;
        for(int diceRoll : allDiceRolls.values()){
            ret += diceRoll;
        }
        return ret;
    }

    public StatisticItemInt getTurnCount() {
        return turnCount;
    }

    public SupplierStatistic getDiceRollCount() {
        return diceRollCount;
    }

    public StatisticItemInt getWasBumped() {
        return wasBumped;
    }

    public StatisticItemInt getHasBumped() {
        return hasBumped;
    }

}
