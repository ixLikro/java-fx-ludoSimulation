package mainWindow.model;

import mainWindow.Controller;
import mainWindow.statistic.StatisticHelper;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Board board;
    private List<Player> allPlayer;
    private Dice dice;

    private int lastTurnIndex;
    private int lastPlayerIndex;

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

        lastPlayerIndex = Color.values().length-1;
        lastPlayerIndex = -1;
    }

    public void nextPlayerMove(){
        int tryCount = 1;

        System.out.println("");

        if(allPlayerFinished()){
            System.out.println("Alle Spieler sind fertig -> Nothing to do O_O");
            return;
        }

        if(getNextPlayer().isCleanedUp(board) == Controller.FIGURE_COUNT){
            //skip
            System.out.println("Spieler "+getCurrentPlayer().getColor().name()+" ist bereits fertig -> Überspringen");
            nextPlayerMove();
            return;
        }

        //increment the turn count
        StatisticHelper.getInstance().getPlayerStatistic(getCurrentPlayer().getColor()).getTurnCount().increment();

        //perform one step
        int diceRoll = dice.rollDice();
        System.out.println("Spieler "+getCurrentPlayer().getColor().name()+" hat eine "+diceRoll+" gewürfelt.");
        //increment the dice roll count
        StatisticHelper.getInstance().getPlayerStatistic(getCurrentPlayer().getColor()).getDiceRollCount().increment();

        boolean hasMoved = getCurrentPlayer().performOneStep(diceRoll, board);

        if(!hasMoved){
            //increment the could not move counter
            StatisticHelper.getInstance().getPlayerStatistic(getCurrentPlayer().getColor()).getCountNotMove().increment();
        }

        while (diceRoll != 6 && getCurrentPlayer().isCleanedUp(board) != -1 && tryCount < 3 && !hasMoved){
            tryCount++;
            diceRoll = dice.rollDice();
            System.out.println("Spieler "+getCurrentPlayer().getColor().name()+" hat eine "+diceRoll+" gewürfelt.");
            //increment the dice roll count
            StatisticHelper.getInstance().getPlayerStatistic(getCurrentPlayer().getColor()).getDiceRollCount().increment();
            getCurrentPlayer().performOneStep(diceRoll, board);
            System.out.println("Spieler "+getCurrentPlayer().getColor().name()+" durfte nochmal, da er kein Spieler draußen hat und aufgeräumt hat."+ " - Dies war sein "+ (tryCount) +". Versuch.");

        }

        while (diceRoll == 6){
            diceRoll = dice.rollDice();
            System.out.println("Spieler "+getCurrentPlayer().getColor().name()+" hat eine "+diceRoll+" gewürfelt.");
            //increment the dice roll count
            StatisticHelper.getInstance().getPlayerStatistic(getCurrentPlayer().getColor()).getDiceRollCount().increment();
            getCurrentPlayer().performOneStep(diceRoll, board);
            System.out.println("Spieler "+getCurrentPlayer().getColor().name()+" durfte nochmal, da er eine 6 gewürfelt hat.");
        }


        if(getCurrentPlayer().isCleanedUp(board) == Controller.FIGURE_COUNT){
            System.out.println("Spieler "+getCurrentPlayer().getColor().name()+" ist in diesem Zug fertig geworden!");
        }
    }

    private boolean allPlayerFinished(){
        for(Player player : allPlayer){
            if(player.isCleanedUp(board) != Controller.FIGURE_COUNT){
                return false;
            }
        }
        return true;
    }

    private Player getNextPlayer(){
        lastPlayerIndex++;
        lastPlayerIndex = lastPlayerIndex % Color.values().length;
        return allPlayer.get(lastPlayerIndex);
    }

    private Player getCurrentPlayer(){
        return allPlayer.get(lastPlayerIndex);
    }

    public List<Player> getAllPlayer() {
        return allPlayer;
    }

    public Dice getDice() {
        return dice;
    }

    public Board getBoard() {
        return board;
    }
}
