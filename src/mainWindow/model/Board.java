package mainWindow.model;

import mainWindow.Controller;
import mainWindow.model.field.Field;
import mainWindow.model.field.FinnishField;
import mainWindow.model.field.StartField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    public static final int FIELD_COUNT = 40;

    private final List<Field> allFields;
    private final Map<Color, List<FinnishField>> allFinnishFields;
    private final Map<Color, List<StartField>> allStartFields;
    private final Map<Color, Field> allNormalSpawnFields;
    private final Map<Color, Field> allLastNormalFields;

    public Board() {
        allFields = new ArrayList<>();
        allFinnishFields = new HashMap<>();
        allStartFields = new HashMap<>();
        allNormalSpawnFields = new HashMap<>();
        allLastNormalFields = new HashMap<>();
    }

    public void setup(){
        allFields.clear();
        for(int i = 0; i < 40; i++){
            allFields.add(new Field(i));
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
                allFinnishFields.get(color).add(new FinnishField(i, color));
            }
        }

        allNormalSpawnFields.clear();
        allNormalSpawnFields.put(Color.GREEN, allFields.get(0));
        allNormalSpawnFields.put(Color.RED, allFields.get(10));
        allNormalSpawnFields.put(Color.BLUE, allFields.get(20));
        allNormalSpawnFields.put(Color.YELLOW, allFields.get(30));

        for(Map.Entry<Color, Field> entry : allNormalSpawnFields.entrySet()){
            allLastNormalFields.put(entry.getKey(),allFields.get((entry.getValue().getIndex() + 39) % FIELD_COUNT));
        }
    }

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
        if(oldField.getIndex() + diceRoll > allLastNormalFields.get(color).getIndex()){

        }



    }

    public List<StartField> getStartFields(Color color){
        return allStartFields.get(color);
    }

    public List<FinnishField> getFinnishFields(Color color){
        return allFinnishFields.get(color);
    }
}
