package pathfindingVisualiser;

import java.util.*;

//its a singleton class //TODO should it be? Same with BoardEditors
public class VisualizationManager {
    public int stepsPerFrame;
    private Board board;        //TODO move it to a separate data class?
    private final static HashMap<String, BoardEditor> mazeGenerators= new HashMap<>();
    private final static HashMap<String, Pathfinder> pathfinders= new HashMap<>();
    public boolean isPerforming = false;
    private BoardEditor currentEditor;
    private static VisualizationManager INSTANCE;
    private static final int FPS_RATIO = 500;
    public static final int MAX_WEIGHT = 100;
    public static final int WEIGHT_CHANGE = 20;
    public static Random random = new Random();

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

    public void init(String editorToStartName) {
        if(pathfinders.containsKey(editorToStartName)){
            Pathfinder toRun = pathfinders.get(editorToStartName);
            if(board.getIsWeighted() && !toRun.canSolveWeighted){
                System.out.println("Can not run this algorithm on a weighted graph.");
                return;
            }
            else currentEditor = toRun;
        }
        else{
            currentEditor = mazeGenerators.get(editorToStartName);
        }
        currentEditor.start(board);
        System.out.println("starting "+editorToStartName);
        isPerforming = true;
    }

    public void resetBoard(boolean clearWeights){
        if(!isPerforming) {
            board.resetBoard(clearWeights);
        }
    }
    public void clearPath(){
        if(!isPerforming) {
            board.clearPath();
        }
    }
    public void setWeights(){
        if(!isPerforming) {
            board.randomizeWeights();
        }
    }
    public void clearWeights(){
        if(!isPerforming) {
            board.clearWeights();
        }
    }

    public void step(){
        if(!currentEditor.isDone()){currentEditor.step();}
        else isPerforming = false;
    }

    public double getCellSize(Vector2 size){
        System.out.println(size+" from getCellSize");
        return Math.min(size.getX()/board.getWidth(), size.getY()/board.getHeight());
    }
}
