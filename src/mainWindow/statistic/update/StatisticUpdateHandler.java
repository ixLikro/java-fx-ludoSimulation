package mainWindow.statistic.update;

import java.util.ArrayList;
import java.util.List;

public class StatisticUpdateHandler {

    private static StatisticUpdateHandler ourInstance;
    private List<Updatable> allObserver;

    private StatisticUpdateHandler(){
        allObserver = new ArrayList<>();
    }

    public static StatisticUpdateHandler getInstance(){
        if(ourInstance == null){
            ourInstance = new StatisticUpdateHandler();
        }
        return ourInstance;
    }


    public void register(Updatable newObserver){
        allObserver.add(newObserver);
    }

    public void unregister(Updatable toRemove){
        allObserver.remove(toRemove);
    }

    public void updateObserver(){
        allObserver.forEach(Updatable::update);
    }

}
