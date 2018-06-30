package mainWindow.statistic;

import java.util.function.Supplier;

public class SupplierStatistic extends StatisticItem {

    private Supplier supplier;

    public SupplierStatistic(String name, Supplier supplier) {
        super(name);
        this.supplier = supplier;
    }

    /**
     * try to return the value as integer. If the supplier dont return an int this method will return -1
     * @return -1 or the int that is given by the supplier
     */
    public int getValueAsInteger(){
        if(supplier.get() instanceof Integer){
            return (Integer) supplier.get();
        }else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return name+supplier.get();
    }

    @Override
    public String getValue() {
        return supplier.get().toString();
    }
}
