package pathfindingVisualiser;

public abstract class MazeGenerator implements BoardEditor{
    protected boolean done;
    protected Board board;
    protected Node startNode;

    @Override   //TODO add abstract class here as well probably
    public void start(Board board, Node startNode) {
        this.startNode = startNode;
        done = false;
        this.board = board;
    }

    protected abstract void mazeStep();

    @Override
    public void step() {
        if(!done) {
            mazeStep();
        }
        else{
            System.out.println("Maze generation complete.");
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
