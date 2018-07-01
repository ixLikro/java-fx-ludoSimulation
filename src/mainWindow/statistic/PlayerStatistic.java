package mainWindow.statistic;


import utility.Round;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String toString(boolean sum) {

        if(sum && StatisticHelper.getInstance().getGameCount() == 0){
            return "Noch kein Spiel fertig abgeschlossen =_=";
        }

        StringBuilder ret = new StringBuilder();
        List<StatisticItem> valuesAsList = getValuesAsList();

        if(sum){
            ret.append("Durchschnittliche Werte von ")
                    .append(StatisticHelper.getInstance().getGameCount())
                    .append(" Spielen:\n\n");
        }

        for (StatisticItem aValuesAsList : valuesAsList) {
            if(!sum){
                ret.append(aValuesAsList.toString());
            }else {
                ret.append(aValuesAsList.name);
                ret.append(Round.to3Digits((double) aValuesAsList.getValueAsInteger()
                        / (double) StatisticHelper.getInstance().getGameCount()));
            }
            ret.append("\n");
        }

        ret.append("\nWürfel Ergebnisse:\n");

        for (Map.Entry<Integer, Integer> entry : allDiceRolls.entrySet()) {
            ret.append("Die ")
                    .append(entry.getKey())
                    .append(" wurde ");
            if(!sum){
                ret.append(entry.getValue());
            }else {
                ret.append(Round.to2Digits(entry.getValue() / (double) StatisticHelper.getInstance().getGameCount()));
            }
            ret.append(" mal gewürfelt")
                    .append("\n");
        }

        return ret.toString();
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public void incrementDiceRoll(int diceRoll){
        allDiceRolls.put(diceRoll, allDiceRolls.get(diceRoll)+1);
    }

    public void incrementDiceRoll(int diceRoll, int incrementBy){
        allDiceRolls.put(diceRoll, allDiceRolls.get(diceRoll)+incrementBy);
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

    public int getOneDiceRollCount(int diceResult){
        return allDiceRolls.get(diceResult);
    }
}
