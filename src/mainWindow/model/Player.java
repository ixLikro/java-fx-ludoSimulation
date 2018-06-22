package mainWindow.model;

import mainWindow.Controller;
import mainWindow.model.field.Field;
import mainWindow.model.field.FinnishField;
import mainWindow.model.field.StartField;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Player {

    //Settings
    private BiConsumer<Integer, Board> moveAlgorithm = this::forceBump;

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

    public void performMove(int diceRoll, Board board){
        lastDiceRoll = diceRoll;
        moveAlgorithm.accept(diceRoll, board);
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
    private void forceBump(Integer diceRoll, Board board){

        Map<Figure, Integer> figureQualityRating = new HashMap<>();
        Player bumpThisPlayer = null;


        for (Figure figure : allFigures){
            Field newField = board.calculateNewField(figure.getIsOn(), color, diceRoll);

            //add to rating
            figureQualityRating.put(figure, 0);

            //check if the figure stand on the normal spwan field (1st prio)
            if(figure.getIsOn().equals(board.getNormalSpawnField(color))){
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
            if(!(newField instanceof FinnishField) && !(newField instanceof StartField)) {
                for (Player player : game.getAllPlayer()) {
                    if (player.getColor() == color) {
                        continue;
                    }

                    if (player.isOnField(newField) != null) {
                        bumpThisPlayer = player;
                    }
                }
                if (bumpThisPlayer != null) {
                    figureQualityRating.replace(figure, 1000);
                    continue;
                }
            }

            //check if this figure can go in the finnish area (4th prio)
            if(newField instanceof FinnishField && !(figure.getIsOn() instanceof FinnishField)){
                boolean valid = true;
                for (int i = 0; i < newField.getIndex(); i++){
                    if(isOnField(board.getFinnishFields(color).get(i)) != null){
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
            if(newField instanceof FinnishField && figure.getIsOn() instanceof FinnishField && !newField.equals(figure.getIsOn())){

                boolean valid = true;
                for (int i = figure.getIsOn().getIndex()+1; i < newField.getIndex(); i++){
                    if(isOnField(board.getFinnishFields(color).get(i)) != null){
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
                        //finally perform move
                        entry.getKey().setIsOn(newField);
                        return;
                    }
                }else {
                    canGoWithoutPrio.add(entry);
                }
            }
        }

        if(canGoWithoutPrio.size() > 0) {
            //set the figur that is nearest on the last normal field
            Map.Entry<Figure, Integer> entry = canGoWithoutPrio.stream()
                    .sorted((o1, o2) -> Integer.compare(board.calculateDiffToLastField(o2.getKey(), color)
                            , board.calculateDiffToLastField(o1.getKey(), color)))
                    .collect(Collectors.toList()).get(0);

            entry.getKey().setIsOn(board.calculateNewField(entry.getKey().getIsOn(), color, diceRoll));
        }
    }


}
