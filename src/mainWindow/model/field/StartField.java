package mainWindow.model.field;

import mainWindow.model.Color;

public class StartField extends SpecialField {

    public StartField(int index, Color color) {
        super(index, color);

    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof StartField)){
            return false;
        }

        if(obj == this){
            return true;
        }

        StartField rhs = (StartField) obj;
        return getIndex() == rhs.getIndex() && getColor().equals(((StartField) obj).getColor());
    }
}

