package pathfindingVisualiser;

public abstract class Pathfinder implements BoardEditor {
    protected boolean isSearching;
    protected boolean isShowingPath;
    protected boolean done;
    protected Node target;
    protected Board board;
    protected Node startNode;
    public boolean canSolveWeighted;

    public Pathfinder(boolean canSolveWeighted){
        this.canSolveWeighted = canSolveWeighted;
    }

    public void start(Board board, Node startNode) {
        this.board = board;
        this.startNode = startNode;
        this.startNode.setDistance(0);
        isSearching = true;
        isShowingPath = false;
        done = false;
    }

    protected abstract void search();

    public void step() {
        if(isSearching) {
            search();
        }
        else if(isShowingPath){
            if(target.getParent() != null){
                target = target.getParent();
                target.trySetState(NodeState.PATH);
            }
            else{
                finishShowingPath();
            }
        }
    }

    protected void finishSearch(Node end){
        isSearching = false;
        isShowingPath = true;
        target = end;
        System.out.println("Found a path with length of "+ target.getDistance());
    }
    protected void abandonSearch(){
        System.out.println("No path exists");
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    private void finishShowingPath(){
        System.out.println("End of showing the path.");
        isShowingPath = false;
        done = true;
    }
}
