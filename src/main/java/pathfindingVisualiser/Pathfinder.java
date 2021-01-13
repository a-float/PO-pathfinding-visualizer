package pathfindingVisualiser;

public abstract class Pathfinder implements BoardEditor {
    protected boolean isSearching;
    protected boolean isShowingPath;
    protected boolean done;
    protected Node target;
    protected Board board;
    protected Node startNode;

    @Override
    public void start(Board board) {
        startNode = board.getStartNode();
        this.board = board;
        isSearching = true;
        isShowingPath = false;
        done = false;
    }

    abstract void search();

    public void step() {
        if(isSearching) {
            search();
        }
        else if(isShowingPath){
            if(target.getParent() != null){
                target = target.getParent();
                if(!target.isStartOrEnd()) {
                    target.setState(NodeState.PATH);
                }
            }
            else{
                finishShowingPath();
            }
        }
    }

    protected void finishSearch(Node end){
        System.out.println("End node has been found");
        isSearching = false;
        isShowingPath = true;
        target = end;
    }
    protected void cancelSearch(){
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
