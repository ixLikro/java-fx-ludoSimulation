package mainWindow.statistic;

import mainWindow.model.Color;
import utility.Round;

import java.util.HashMap;
import java.util.Map;

public class FieldStatistic {

    private Map<Color, StatisticItemInt> allPlayerCount;

    public FieldStatistic() {
        allPlayerCount = new HashMap<>();

        for(Color color : Color.values()){
            allPlayerCount.put(color, new StatisticItemInt(color.name()+": "));
        }
    }

    public void increment(Color color){
        StatisticItem item = allPlayerCount.get(color);
        if(item != null){
            ((StatisticItemInt) item).increment();
        }
    }

    public void increment(Color color, int value){
        StatisticItem item = allPlayerCount.get(color);
        if(item != null){
            ((StatisticItemInt) item).increment(value);
        }
    }

    public StatisticItemInt getOneStatItem(Color color){
        return allPlayerCount.get(color);
    }

    public Map<Color, StatisticItemInt> getAllPlayerCount() {
        return allPlayerCount;
    }

    public void setValue(Color color, int value){
        StatisticItem item = allPlayerCount.get(color);
        if(item != null){
            ((StatisticItemInt) item).setValue(value);
        }
    }

    public String toString(boolean sum) {

        if(sum && StatisticHelper.getInstance().getGameCount() == 0){
            return "Noch kein Spiel fertig abgeschlossen =_=";
        }


        StringBuilder ret = new StringBuilder();

        if(sum){
            ret.append("Durchschnittliche Werte von ")
                    .append(StatisticHelper.getInstance().getGameCount())
                    .append(" Spielen\n\n");
        }

        ret.append("So oft haben die Spieler eine\nFigur auf dieses Feld gesetzt:\n");

        Color[] values = Color.values();
        for (int i = 0; i < values.length; i++) {
            Color color = values[i];
            if(!sum){
                ret.append(allPlayerCount.get(color).toString());
            }else {
                ret.append(allPlayerCount.get(color).name);
                ret.append(Round.to3Digits((double) allPlayerCount.get(color).getValueAsInteger()
                        / (double) StatisticHelper.getInstance().getGameCount()));
            }
            if (i != values.length - 1) {
                ret.append("\n");
            }
        }

        return ret.toString();
    }

    @Override
    public String toString() {
        return toString(false);
    }
}
