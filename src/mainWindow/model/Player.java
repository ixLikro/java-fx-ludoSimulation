package mainWindow.model;

import mainWindow.Controller;
import mainWindow.model.field.Field;
import mainWindow.model.field.FinishField;
import mainWindow.model.field.StartField;
import mainWindow.statistic.StatisticHelper;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Player {

    //Settings
    private BiFunction<Integer, Board, Boolean> moveAlgorithm = this::forceBump;

    private List<Figure> allFigures;
    private Color color;
    private int lastDiceRoll;

    private Game game;


    public Player(Color color, Game game) {
        this.color = color;
        this.game = game;

        allFigures = new ArrayList<>();
    }

    public void setup(List<StartField> startFields){

        allFigures.clear();
        for (int i = 0;  i < Controller.FIGURE_COUNT ; i++) {
            allFigures.add(new Figure(startFields.get(i)));
        }
    }

    /**
     * return the figure that stand on the given field, or null
     * @param field the field
     * @return the figure or null
     */
    public Figure isOnField(Field field){

        for (Figure figure : allFigures) {
            if (figure.getIsOn().equals(field)) {
                return figure;
            }
        }

        return null;
    }

    public boolean performOneStep(int diceRoll, Board board){
        lastDiceRoll = diceRoll;

        //increment the dice roll count
        StatisticHelper.getInstance().getCurrentGameStat()
                .getPlayerStatistic(color).incrementDiceRoll(diceRoll);

        return moveAlgorithm.apply(diceRoll, board);
    }

    /**
     * @param board the board
     * @return true if no figure in in the house(start fields)
     */
    public boolean isHouseFree(Board board){

        for(StartField startField : board.getStartFields(color)){
            if(isOnField(startField) != null){
                return false;
            }
        }

        return true;
    }

    public int isCleanedUp(Board board){

        int figureCountOnNormal = 0, figureCountOnFinnish = 0, figureCountOnStart = 0;
        for (Figure figure : allFigures) {
            if (figure.getIsOn() instanceof FinishField){
                figureCountOnFinnish++;
                continue;
            }

            if(figure.getIsOn() instanceof StartField){
                figureCountOnStart++;
                continue;
            }

            figureCountOnNormal++;
        }

        //if at least one figure on a normal field -> return false (-1)
        if(figureCountOnNormal != 0){
            return -1;
        }

        //go back from the last finnish field to the first. if the is a gap then return false
        for(int i = 0; i < figureCountOnFinnish; i++){
            FinishField field = board.getFinishFields(color).get(Controller.FIGURE_COUNT - i -1); //-1 bc the finnish fields are index based
            if(isOnField(field) == null){
                return -1;
            }
        }

        return figureCountOnFinnish;
    }

    public List<Figure> getAllFigures() {
        return allFigures;
    }

    public Color getColor() {
        return color;
    }

    public int getLastDiceRoll() {
        return lastDiceRoll;
    }

    @Override
    public String toString() {
        return "Player{" +
                "allFigures=" + allFigures +
                ", color=" + color +
                '}';
    }

    /**
     * this method perform the actual move and bump a enemy
     * @param figure the figure that should move
     * @param newField the new field
     * @param bumpPlayer a player that stand on the new Field, or null
     * @param board the board
     */
    private boolean setNewField(Figure figure, Field newField, Player bumpPlayer, Board board){

        if(figure.getIsOn().equals(newField)){
            return false;
        }

        if(bumpPlayer != null){
            //get the actual figure to bump
            Figure toBump = bumpPlayer.isOnField(newField);

            //bump the figure
            toBump.setIsOn(board.getFreeStartField(bumpPlayer));

            System.out.println("Spieler "+color.name()+" hat eine Firgur von "+bumpPlayer.getColor().name()+" rausgeworfen!");

            //increment the was bumped
            StatisticHelper.getInstance().getCurrentGameStat()
                    .getPlayerStatistic(bumpPlayer.getColor()).getWasBumped().increment();
            //inclement the has bumped counter
            StatisticHelper.getInstance().getCurrentGameStat()
                    .getPlayerStatistic(color).getHasBumped().increment();
        }


        if(!figure.getIsOn().equals(newField)){
            //increment the field count
            if(!(newField instanceof StartField) && !(newField instanceof FinishField)){
                StatisticHelper.getInstance().getCurrentGameStat()
                        .getFieldStatistic(newField.getIndex()).increment(color);
            }
        }

        //now set our figure
        figure.setIsOn(newField);
        return true;
    }

    //****************************Algorithms

    /**
     *
     *    1st prio: clear the normal spawn field [10.000]
     *    2nd prio: getOut(from StartField) [5.000]
     *    3rd prio: bump enemy pawn back [1.000]
     *    4th prio: go in the finnish area [500]
     *    4th prio: move up in finnish fields[100]
     *
     *    Try to perform a normal move beginning from the first pawn
     *
     * @param diceRoll the diceRoll
     * @param board the actual board
     */
    private boolean forceBump(Integer diceRoll, Board board){

        Map<Figure, Integer> figureQualityRating = new HashMap<>();
        Map<Figure, Player> bumpThisPlayer = new HashMap<>();


        for (Figure figure : allFigures){
            Field newField = board.calculateNewField(figure.getIsOn(), color, diceRoll);
            //check if we can bump an enemy pawn
            if(!(newField instanceof FinishField) && !(newField instanceof StartField)) {
                for (Player player : game.getAllPlayer()) {
                    if (player.getColor() == color) {
                        continue;
                    }

                    if (player.isOnField(newField) != null) {
                        bumpThisPlayer.put(figure, player);
                    }
                }
            }


            //add to rating
            figureQualityRating.put(figure, 0);

            //check if the figure stand on the normal spawn field (1st prio)
            if(figure.getIsOn().equals(board.getNormalSpawnField(color)) && !isHouseFree(board)){
                figureQualityRating.replace(figure, 10000);
                continue;
            }

            //check if we can go out (2nd prio)
            if(figure.getIsOn() instanceof StartField){
                if(!(newField instanceof StartField)){
                    figureQualityRating.replace(figure, 5000);
                    continue;
                }
            }

            //check if we can bump an enemy pawn (3rd prio)
            if(bumpThisPlayer.get(figure) != null){
                figureQualityRating.replace(figure, 1000);
                continue;
            }

            //check if this figure can go in the finnish area (4th prio)
            if(newField instanceof FinishField && !(figure.getIsOn() instanceof FinishField)){
                boolean valid = true;
                for (int i = 0; i < newField.getIndex(); i++){
                    if(isOnField(board.getFinishFields(color).get(i)) != null){
                        valid = false;
                    }
                }
                if(valid){
                    figureQualityRating.replace(figure, 500);
                }else {
                    figureQualityRating.replace(figure, -500);
                }
                continue;
            }

            //check if this figure can move up in the finnish area (5th prio)
            if(newField instanceof FinishField && figure.getIsOn() instanceof FinishField && !newField.equals(figure.getIsOn())){

                boolean valid = true;
                for (int i = figure.getIsOn().getIndex()+1; i < newField.getIndex(); i++){
                    if(isOnField(board.getFinishFields(color).get(i)) != null){
                        valid = false;
                    }
                }
                if(valid){
                    figureQualityRating.replace(figure, 100);

                }else {
                    figureQualityRating.replace(figure, -100);
                }
                continue;
            }
        }




        //sort the rating
        List<Map.Entry<Figure, Integer>> sort = figureQualityRating.entrySet()
                .stream()
                .sorted((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()))
                .collect(Collectors.toList());

        List<Map.Entry<Figure, Integer>> canGoWithoutPrio = new ArrayList<>();

        //if we have a prio move -> go for it!
        for(Map.Entry<Figure, Integer> entry : sort){
            Field newField = board.calculateNewField(entry.getKey().getIsOn(), color, diceRoll);
            if (isOnField(newField) == null){
                if(entry.getValue() != 0){
                    if(entry.getValue() > 0){
                        //finally perform prio move
                        return setNewField(entry.getKey(), newField, bumpThisPlayer.get(entry.getKey()), board);
                    }
                }else {
                    canGoWithoutPrio.add(entry);
                }
            }
        }

        if(canGoWithoutPrio.size() > 0) {
            //set the figur that is nearest on the last normal field
            Map.Entry<Figure, Integer> entry = canGoWithoutPrio.stream()
                    .sorted(Comparator.comparingInt(o -> board.calculateDiffToLastField(o.getKey().getIsOn(), color)))
                    .collect(Collectors.toList()).get(0);
            //finally perform move
            return setNewField(entry.getKey(), board.calculateNewField(entry.getKey().getIsOn(), color, diceRoll), bumpThisPlayer.get(entry.getKey()), board);
        }


        return false;
    }
}
