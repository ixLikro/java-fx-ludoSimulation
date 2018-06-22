package mainWindow.model.field;

import mainWindow.model.Color;

public class FinnishField extends SpecialField {

    public FinnishField(int index, Color color) {
        super(index, color);
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FinnishField)){
            return false;
        }

        if(obj == this){
            return true;
        }

        FinnishField rhs = (FinnishField) obj;
        return getIndex() == rhs.getIndex() && getColor().equals(((FinnishField) obj).getColor());
    }
}
