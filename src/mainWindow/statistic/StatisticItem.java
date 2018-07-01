package mainWindow.statistic;

public abstract class StatisticItem {

    protected String name;

    public StatisticItem(String name){
        this.name = name;
    }

    public abstract String getValue();

    public abstract int getValueAsInteger();


}
