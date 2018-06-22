package mainWindow.model.field;

public class Field {

   private int index;

    public Field(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }



    @Override
    public String toString() {
        return "Field {" +
                "index=" + index +
                "("+ this.getClass().getSimpleName()+")}";
    }
}
