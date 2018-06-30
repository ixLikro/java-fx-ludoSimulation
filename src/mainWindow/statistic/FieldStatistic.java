package mainWindow.statistic;

import mainWindow.model.Color;
import mainWindow.model.Game;

import java.util.HashMap;
import java.util.List;
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

    public void setValue(Color color, int value){
        StatisticItem item = allPlayerCount.get(color);
        if(item != null){
            ((StatisticItemInt) item).setValue(value);
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("So oft haben die Spieler eine\nFigur auf dieses Feld gesetzt:\n");

        Color[] values = Color.values();
        for (int i = 0; i < values.length; i++) {
            Color color = values[i];
            ret.append(allPlayerCount.get(color).toString());
            if (i != values.length - 1) {
                ret.append("\n");
            }
        }

        return ret.toString();
    }
}
