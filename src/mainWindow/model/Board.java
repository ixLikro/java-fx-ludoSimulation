package mainWindow.model;

import mainWindow.Controller;
import mainWindow.model.field.Field;
import mainWindow.model.field.FinishField;
import mainWindow.model.field.StartField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    public static final int FIELD_COUNT = 40;

    private final List<Field> allNormalFields;
    private final Map<Color, List<FinishField>> allFinnishFields;
    private final Map<Color, List<StartField>> allStartFields;
    private final Map<Color, Field> allNormalSpawnFields;
    private final Map<Color, Field> allLastNormalFields;

    public Board() {
        allNormalFields = new ArrayList<>();
        allFinnishFields = new HashMap<>();
        allStartFields = new HashMap<>();
        allNormalSpawnFields = new HashMap<>();
        allLastNormalFields = new HashMap<>();
    }

    public void setup(){
        allNormalFields.clear();
        for(int i = 0; i < 40; i++){
            allNormalFields.add(new Field(i));
        }

        allStartFields.clear();
        for(Color color : Color.values()){
            allStartFields.put(color, new ArrayList<>());
            for (int i = 0; i < Controller.FIGURE_COUNT; i++) {
                allStartFields.get(color).add(new StartField(i, color));
            }
        }

        allFinnishFields.clear();
        for(Color color : Color.values()){
            allFinnishFields.put(color, new ArrayList<>());
            for (int i = 0; i < Controller.FIGURE_COUNT; i++) {
                allFinnishFields.get(color).add(new FinishField(i, color));
            }
        }

        allNormalSpawnFields.clear();
        allNormalSpawnFields.put(Color.GREEN, allNormalFields.get(0));
        allNormalSpawnFields.put(Color.RED, allNormalFields.get(10));
        allNormalSpawnFields.put(Color.BLUE, allNormalFields.get(20));
        allNormalSpawnFields.put(Color.YELLOW, allNormalFields.get(30));

        for(Map.Entry<Color, Field> entry : allNormalSpawnFields.entrySet()){
            allLastNormalFields.put(entry.getKey(), allNormalFields.get((entry.getValue().getIndex() + 39) % FIELD_COUNT));
        }
    }

    /**
     * this method calculates the new field for one figure.
     * it ignores other figures, but it follows the game rules
     *
     * @param oldField the old field
     * @param color the color(it's needed bc the start/finnish fields are diffrent for every color)
     * @param diceRoll the erg from the dice roll
     * @return the new field
     */
    public Field calculateNewField(Field oldField, Color color, int diceRoll){

        //can the player go to an normal field
        if(oldField instanceof StartField){
            if(diceRoll != 6){
                return oldField;
            }else {
                return allNormalSpawnFields.get(color);
            }
        }

        //check if the new field is a finnish field
        int diffToLastField = calculateDiffToLastField(oldField, color);
        if(diffToLastField < diceRoll && diffToLastField != -1 && !(oldField instanceof FinishField)){
            //calculate the finnish index, -1 bc the finnish fields start with 0
            int index = diceRoll -diffToLastField -1;

            if(index > (Controller.FIGURE_COUNT-1)){
                return oldField;
            }
            return allFinnishFields.get(color).get(index);
        }

        //check the finnish area
        if(oldField instanceof FinishField){
            if(oldField.getIndex() + diceRoll >= Controller.FIGURE_COUNT){
                return oldField;
            }else {
                return allFinnishFields.get(color).get(oldField.getIndex() + diceRoll);
            }
        }

        //if we are still here we return the normal field
        return allNormalFields.get((oldField.getIndex() + diceRoll) % FIELD_COUNT);
    }

    /**
     * @param field the field the figure stand on
     * @param color the color of the player
     * @return the diff to the last normal field, if(unvalid) return -1
     */
    public int calculateDiffToLastField(Field field, Color color){
        if(field instanceof StartField || field instanceof FinishField){
            return -1;
        }

        int count = 0;
        Field pivo = field;
        while (!pivo.equals(getLastNormalField(color))){
            count++;
            pivo = allNormalFields.get((field.getIndex() + count) % 40);

            if(count > 80) return -1;
        }
        return count;
    }

    /**
     * @param player the player
     * @return a free startField from a specific color, or null if all start fields a occupied
     */
    public Field getFreeStartField(Player player){
        for (StartField field : allStartFields.get(player.getColor())) {
            if (player.isOnField(field) == null) {
                return field;
            }
        }

        return null;
    }

    public List<StartField> getStartFields(Color color){
        return allStartFields.get(color);
    }

    public List<FinishField> getFinishFields(Color color){
        return allFinnishFields.get(color);
    }

    public Field getNormalSpawnField(Color color){
        return allNormalSpawnFields.get(color);
    }

    public Field getLastNormalField(Color color){
        return allLastNormalFields.get(color);
    }

    public List<Field> getAllNormalFields() {
        return allNormalFields;
    }
}
