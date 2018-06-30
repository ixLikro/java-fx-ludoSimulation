package mainWindow;

import javafx.application.Platform;
import javafx.beans.WeakListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.control.Slider;

import java.util.concurrent.atomic.AtomicInteger;

public class Timer extends Thread {

    final private Thread worker;

    private AtomicInteger wait;
    private ChangeListener<Number> sliderListener;

    public Timer(Thread worker, Slider slider) {
        this.worker = worker;
        wait = new AtomicInteger();


        sliderListener = (observable, oldValue, newValue) -> {
            wait.set(calcWait(newValue.intValue()));
            System.out.println("slider: " + newValue.intValue() + ", wait: " + calcWait(newValue.intValue()));
        };

        wait.set(calcWait(slider.getValue()));

        slider.valueProperty().addListener(new WeakChangeListener<>(sliderListener));
    }

    @Override
    public void run() {

        while (!Thread.interrupted()) {
            synchronized (worker) {
                worker.notify();
            }
            try {
                Thread.sleep(wait.get());
            } catch (InterruptedException e) {
                return;
            }
        }

    }

    private int calcWait(double turnPerSecond){
         return (int)((1. /turnPerSecond)*1000.);
    }
}
