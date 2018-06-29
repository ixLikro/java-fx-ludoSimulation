package mainWindow.statistic;

public class StatisticItemInt extends StatisticItem {

    private int value;

    public StatisticItemInt(String name) {
        super(name);
        value = 0;
    }

    public void increment(){
        value++;
    }

    public void increment(int i){
        value += i;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValueAsInt(){
        return value;
    }

    @Override
    public String getValue() {
        return value+"";
    }

    @Override
    public String toString() {
        return name + getValue();
    }
}
