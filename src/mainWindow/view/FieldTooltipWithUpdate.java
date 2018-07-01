package mainWindow.view;

import javafx.scene.Node;
import mainWindow.model.Color;
import mainWindow.statistic.StatisticHelper;
import mainWindow.statistic.update.StatisticUpdateHandler;

public class FieldTooltipWithUpdate extends TooltipWithUpdate {
    private int fieldIndex;

    public FieldTooltipWithUpdate(Node installAt, int fieldIndex) {
        super(installAt);


        this.fieldIndex = fieldIndex;
        tooltip.setOnShowing(event ->{
            StatisticUpdateHandler.getInstance().register(this);

            tooltip.setText(StatisticHelper.getInstance().getSumStatistic().getFieldStatistic(fieldIndex).toString(true));
        });
        tooltip.setOnHiding(event ->{
            StatisticUpdateHandler.getInstance().unregister(this);
        });
    }

    @Override
    public void update() {
        tooltip.setText(StatisticHelper.getInstance().getSumStatistic().getFieldStatistic(fieldIndex).toString(true));
    }
}
