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

    private List<Field> allFields;
    private Map<Color, List<FinnishField>> allFinnishFields;
    private Map<Color, List<StartField>> allStartFields;

    public Board() {
        allFields = new ArrayList<>();
        allFinnishFields = new HashMap<>();
        allStartFields = new HashMap<>();
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
    }

    public List<StartField> getStartFields(Color color){
        return allStartFields.get(color);
    }

    public List<FinnishField> getFinnishFields(Color color){
        return allFinnishFields.get(color);
    }
}
