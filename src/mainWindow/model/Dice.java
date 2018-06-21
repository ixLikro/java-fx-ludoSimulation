package mainWindow.model;

import java.util.Random;

public class Dice {
    private Random random = new Random();

    public Dice() {
        this.random =  new Random();
    }

    public int rollDice(){
        return getNumber(1,6);
    }

    private int getNumber(int min, int max){
        return random.nextInt(max - min + 1) + min;
    }


}
