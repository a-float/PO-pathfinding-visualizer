package pathfindingVisualiser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class LegendController implements Initializable{
    @FXML
    GridPane legendGridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int i = 0;
        for(NodeState state: NodeState.values()){
            Rectangle rect = new Rectangle(17,17,NodeState.colorMap.get(state));
            Label label = new Label();
            label.setMaxWidth(250);
            label.setText(NodeState.stateDescriptionMap.get(state));
            Separator separator = new Separator();
            separator.setOrientation(Orientation.VERTICAL);
            legendGridPane.add(rect, 0, i, 1,1);
            legendGridPane.add(label,1, i, 1,1);
            i++;
        }
    }
}
