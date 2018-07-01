package mainWindow.view;

import javafx.scene.Node;
import mainWindow.model.Color;
import mainWindow.statistic.StatisticHelper;
import mainWindow.statistic.update.StatisticUpdateHandler;

public class PlayerTooltipWithUpdate extends TooltipWithUpdate {
    private Color color;

    public PlayerTooltipWithUpdate(Node installAt, Color color) {
        super(installAt);

        this.color = color;
        tooltip.setOnShowing(event ->{
            StatisticUpdateHandler.getInstance().register(this);

            tooltip.setText(StatisticHelper.getInstance().getSumStatistic().getPlayerStatistic(color).toString(true));
        });
        tooltip.setOnHiding(event ->{
            StatisticUpdateHandler.getInstance().unregister(this);
        });
    }

    @Override
    public void update() {
        tooltip.setText(StatisticHelper.getInstance().getSumStatistic().getPlayerStatistic(color).toString(true));
    }
}
