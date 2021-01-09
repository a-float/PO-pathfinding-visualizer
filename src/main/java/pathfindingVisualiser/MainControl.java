package pathfindingVisualiser;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainControl implements Initializable {
    private static final Map<NodeState, Color> colorMap;
    static {
        colorMap = Map.of(NodeState.FREE, Color.WHITE,
                NodeState.START, Color.GREEN,
                NodeState.END, Color.ORANGERED,
                NodeState.WALL, Color.BLACK,
                NodeState.VISITED, Color.DARKBLUE,
                NodeState.BUSY, Color.CYAN);
    }

    @FXML
    Canvas boardCanvas;
    @FXML
    ChoiceBox algChoiceBox;
    @FXML
    ChoiceBox mazeChoiceBox;
    private VisualizationManager manager;
    private boolean isPlaying = false;

    @FXML
    private void step(ActionEvent event){
        if(!manager.isPerforming) {
            System.out.println(algChoiceBox.getValue());
            if(algChoiceBox.getValue()!= null) {
                manager.init(algChoiceBox.getValue().toString());  //TODO definietely make another class
            }
        }
        else{
            manager.step();
            showBoard();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Board board = new Board(20,5);
        manager = VisualizationManager.getInstance();
        manager.setBoard(board);

        mazeChoiceBox.getItems().addAll(manager.getMazeNames());
        algChoiceBox.getItems().addAll(manager.getAlgsNames());
        System.out.println(board);
        Platform.runLater(this::showBoard);
    }
//    @FXML
//    private void createTimer(double seconds) {
//        if (timer != null) {
//            timer.stop();
//        }
//        timer = new Timeline(new KeyFrame(Duration.seconds(seconds), e -> nextGen()));
//        timer.setCycleCount(Timeline.INDEFINITE);
//        if(isPlaying)timer.play();
//    }

    public void showBoard() { //TODO hardcoded colors
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        Bounds bounds = boardCanvas.getBoundsInLocal();
        Board board = manager.getBoard();
        double cellSize = Math.min(bounds.getWidth()/board.getWidth(), bounds.getHeight()/board.getHeight());
        gc.clearRect(0, 0, bounds.getWidth(), bounds.getHeight());

        for(int x = 0; x < board.getWidth(); x++){
            for(int y = 0; y < board.getHeight(); y++){
                Node node = board.getNodeAt(new Vector2(x,y));
                gc.setFill(colorMap.get(node.getState()));
                gc.fillRect(node.getPosition().getX()*cellSize, node.getPosition().getY()*cellSize, cellSize, cellSize);
            }
        }
    }
}
