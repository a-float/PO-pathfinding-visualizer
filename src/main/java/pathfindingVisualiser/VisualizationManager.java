package pathfindingVisualiser;

import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

//its a singleton class
public class VisualizationManager {
    public int stepsPerFrame;
    private Board board;
    private final static HashMap<String, MazeGenerator> mazeGenerators= new HashMap<>();
    private final static HashMap<String, Pathfinder> pathfinders= new HashMap<>();
    public boolean isPerforming = false;
    private BoardEditor currentEditor;
    private static VisualizationManager INSTANCE;
    private static final int FPS_RATIO = 500;
    public static final int MAX_WEIGHT = 100;
    public static final int WEIGHT_CHANGE = 20;
    public static Random random = new Random(System.currentTimeMillis());
    private final UserWanderer wanderer = new UserWanderer();

    private VisualizationManager(){
        mazeGenerators.put("Random Maze", new RandomMazeGenerator());
        mazeGenerators.put("Recursive Maze", new RecursiveMazeGenerator());
        mazeGenerators.put("Backtracking Maze", new BacktrackingMazeGenerator());
        pathfinders.put("Dijkstra's algorithm", new DijkstraAlgorithm());
        pathfinders.put("A* algorithm", new AstarAlgorithm());
        pathfinders.put("DFS algorithm", new DfsAlgorithm());
        pathfinders.put("BFS algorithm", new BfsAlgorithm());
    }

    public static VisualizationManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new VisualizationManager();
        }
        return INSTANCE;
    }

    public void setBoard(Board board){
        this.stepsPerFrame = Math.max(1, board.getNodeCount()/FPS_RATIO);
        this.board = board;
        wanderer.reset();
        wanderer.setBoard(board);
    }

    public Board getBoard() {
        return board;
    }
    public List<String> getMazeNames(){
        return new ArrayList<>(mazeGenerators.keySet());
    }
    public List<String> getAlgsNames(){
        return new ArrayList<>(pathfinders.keySet());
    }

    public void init(String editorToStartName, boolean isPathfinder) {
        if(!isPerforming) {
            if (isPathfinder) {
                Pathfinder toRun = pathfinders.get(editorToStartName);
                if (board.getIsWeighted() && !toRun.canSolveWeighted) {
                    System.out.println("Can not run this algorithm on a weighted graph.");
                    return;
                } else currentEditor = toRun;
            } else {
                currentEditor = mazeGenerators.get(editorToStartName);
            }
            currentEditor.start(board, wanderer.getNodeToStartFrom());
            System.out.println("Starting " + editorToStartName);
            isPerforming = true;
        }
        else{
            System.out.println("Can't start another generator while performing.");
        }
    }

    public void resetBoard(boolean clearWeights){
        if(!isPerforming) {
            wanderer.reset();
            board.resetBoard(clearWeights);
        }
        else{
            System.out.println("Can't reset the board while performing.");
        }
    }
    public void clearPath(){
        if(!isPerforming) {
            board.clearPath();
        }
        else{
            System.out.println("Can't clear path while performing.");
        }
    }
    public void setWeights(){
        if(!isPerforming) {
            board.randomizeWeights();
        }
        else{
            System.out.println("Can't set weights while performing.");
        }
    }
    public void clearWeights(){
        if(!isPerforming) {
            board.clearWeights();
        }
        else{
            System.out.println("Can't clear weights while performing.");
        }
    }

    public void step(){
        if(!currentEditor.isDone()){currentEditor.step();}
        else isPerforming = false;
    }

    public double getMaxCellSize(Vector2 size){
        return Math.min((double)size.getX()/(double)board.getWidth(), (double)size.getY()/(double)board.getHeight());
    }

    public void changeBoardSize(Vector2 size){
        if(!isPerforming){
            System.out.println("Changing board size to "+size);
            setBoard(new Board(size.getX(), size.getY()));
        }
        else{
            System.out.println("Can't change the board size while performing.");
        }
    }

    public boolean flipWall(Vector2 pos) {
        board.getNodeAt(pos).flipWall();
        return board.getNodeAt(pos).getState() == NodeState.WALL;   //if its a wall now, it has been flipped to a wall
    }

    public void flipToWall(Vector2 pos, boolean toWall){
        if(board.isInBounds(pos)) {
            NodeState targetState = toWall ? NodeState.WALL : NodeState.FREE;
            board.getNodeAt(pos).trySetState(targetState);
        }
    }

    public boolean handleKeyPress(KeyEvent key) {
        return wanderer.handleKey(key);
    }

    public void clearWandererPath() {
        wanderer.clearPath();
    }

    public void cancelCurrent() {
        System.out.println("Cancelling current visualization.");
        currentEditor = null;
        isPerforming = false;
    }
}
