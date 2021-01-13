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

import java.awt.*;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainControl implements Initializable {
    private static final Map<NodeState, Color> colorMap;
    private Timeline timer;
    static {
        colorMap = Map.of(NodeState.FREE, Color.LAVENDER,
                NodeState.START, Color.GREEN,
                NodeState.END, Color.ORANGERED,
                NodeState.WALL, Color.BLACK,
                NodeState.VISITED, Color.DARKBLUE,
                NodeState.BUSY, Color.CYAN,
                NodeState.PATH, Color.YELLOW);
    }

    @FXML
    Canvas boardCanvas;
    @FXML
    ChoiceBox<String> algChoiceBox, mazeChoiceBox;
    private VisualizationManager manager;
    private boolean isPlaying = false;

    @FXML
    private void playPause(ActionEvent event){
        if(isPlaying)timer.pause();
        else timer.play();
        isPlaying = !isPlaying;
    }

    @FXML
    private void step(ActionEvent event){
        //the animation is not paused and there is an active board editor
        if(isPlaying) {
            if(manager.isPerforming) {
                for(int i = 0; i < manager.stepsPerFrame; i++) {
                    manager.step();
                }
                showBoard();
            }
            else{
                playPause(new ActionEvent());
            }
        }
    }

    @FXML
    private void performOneStep(ActionEvent event){
        if(manager.isPerforming){
            manager.step();
            showBoard();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Board board = new Board(63,36); //canvas is 7:4
        manager = VisualizationManager.getInstance();
        manager.setBoard(board);

        mazeChoiceBox.getItems().addAll(manager.getMazeNames());
        algChoiceBox.getItems().addAll(manager.getAlgsNames());

        createTimer(0.05);
        setUpCanvasSize();
        Platform.runLater(this::showBoard);
    }

    private void setUpCanvasSize(){
        Vector2 parentSize = Vector2.zero();
        parentSize.setX((int)boardCanvas.getBoundsInParent().getWidth());
        parentSize.setY((int)boardCanvas.getBoundsInParent().getHeight());
        double cellSize = manager.getCellSize(parentSize);
        System.out.println(parentSize);
        System.out.println(cellSize);
        System.out.println("x size "+Math.round(parentSize.getX()*cellSize));
        boardCanvas.setWidth(Math.round(manager.getBoard().getWidth()*cellSize));
        boardCanvas.setHeight(Math.round(manager.getBoard().getHeight()*cellSize));
    }
    private void createTimer(double seconds) {
        if (timer != null) {
            timer.stop();
        }
        timer = new Timeline(new KeyFrame(Duration.seconds(seconds), this::step));
        timer.setCycleCount(Timeline.INDEFINITE);
        if(isPlaying)timer.play();
    }

    private void startGeneration(ChoiceBox<String> generatorChoiceBox){ //TODO ChoiceBox should hold BoardEditors
        if(!manager.isPerforming) {
            System.out.println(generatorChoiceBox.getValue());
            if(generatorChoiceBox.getValue()!= null) {
                manager.init(generatorChoiceBox.getValue().toString());
//                playPause(new ActionEvent());
            }
        }
    }

    public void showBoard() { //TODO hardcoded colors
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        Bounds bounds = boardCanvas.getBoundsInLocal();
        Board board = manager.getBoard();
        double cellSize = Math.min(bounds.getWidth()/board.getWidth(), bounds.getHeight()/board.getHeight());
//        gc.setFill(Color.LAVENDER);     //TODO dont need to clear if its all covered again anyway
//        gc.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
//        gc.clearRect(0, 0, bounds.getWidth(), bounds.getHeight());

        for(int x = 0; x < board.getWidth(); x++){
            for(int y = 0; y < board.getHeight(); y++){
                Node node = board.getNodeAt(new Vector2(x,y));
                gc.setFill(colorMap.get(node.getState()));
                gc.fillRect(node.getPosition().getX()*cellSize, node.getPosition().getY()*cellSize, cellSize, cellSize);
            }
        }
    }

    @FXML
    private void startPathfinding(ActionEvent event){
        manager.clearPath();
        startGeneration(algChoiceBox);
    }
    @FXML
    private void startMazeGeneration(ActionEvent event){
        manager.resetBoard();
        startGeneration(mazeChoiceBox);
    }
    @FXML
    void clearPath(ActionEvent event){
        manager.clearPath();
        showBoard();
    }
    @FXML
    void resetBoard(ActionEvent event){
        manager.resetBoard();
        showBoard();
    }
}
