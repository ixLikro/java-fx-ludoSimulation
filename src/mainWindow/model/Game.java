package mainWindow.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Board board;
    private List<Player> allPlayer;
    private Dice dice;

    private Color lastTurn;

    public Game() {
        this.board =  new Board();
        this.allPlayer = new ArrayList<>();
        this.dice = new Dice();

        board.setup();
        for (Color color : Color.values()){
            Player temp = new Player(color, this);
            temp.setup(board.getStartFields(color));
            allPlayer.add(temp);
        }
    }

    public void nextPlayerMove(){
        getAllPlayer().get(0).performMove(dice.rollDice(), board);

        //todo at 6 again
    }

    public List<Player> getAllPlayer() {
        return allPlayer;
    }

    public Dice getDice() {
        return dice;
    }
}
