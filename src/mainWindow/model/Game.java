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
            Player temp = new Player(color);
            temp.setup(board.getStartFields(color));
            allPlayer.add(temp);
        }
    }
}
