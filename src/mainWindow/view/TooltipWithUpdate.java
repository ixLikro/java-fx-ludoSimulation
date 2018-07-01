package mainWindow.view;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.util.Duration;
import mainWindow.statistic.StatisticHelper;
import mainWindow.statistic.update.StatisticUpdateHandler;
import mainWindow.statistic.update.Updatable;

public abstract class TooltipWithUpdate implements Updatable {

    protected Tooltip tooltip;

    public TooltipWithUpdate(Node installAt) {
        tooltip = new Tooltip();
        tooltip.setShowDelay(Duration.millis(50));
        tooltip.setText("as");
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setFont(new Font(tooltip.getFont().getFamily(),14));


        Tooltip.install(installAt, tooltip);
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    @Override
    abstract public void update();
}
