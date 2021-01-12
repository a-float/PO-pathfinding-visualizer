package pathfindingVisualiser;

import java.util.PriorityQueue;

public class DijkstraAlgorithm implements BoardEditor{
    private final PriorityQueue<Node> queue = new PriorityQueue<>();
    private boolean isSearching;
    private boolean isShowingPath;
    private boolean done;
    private Node target;

    @Override
    public void start(Board board) {    //TODO doesnt actually need the board, maybe no pathfinder does
        queue.clear();
        Node startNode = board.getStartNode();
        startNode.setDistance(0);
        relax(startNode);
        done = false;
        isShowingPath = false;
        isSearching = true;
        target = null;
    }

    private void relax(Node relaxNode){
        for(Node node: relaxNode.getNeighbours()){
            double newDistance = relaxNode.getDistance() + node.getWeight();
            if(newDistance < node.getDistance()) {
                node.setParent(relaxNode);
                node.setDistance(newDistance);
                if(!node.isStartOrEnd()) {
                    node.setState(NodeState.BUSY);
                }
                queue.add(node);
            }
        }
        if(!relaxNode.isStartOrEnd()){
            relaxNode.setState(NodeState.VISITED);
        }
    }

    @Override
    public void step(){
        if(isSearching) {
//            System.out.println(queue);
            Node node = queue.poll(); //returns null when queue empty
            if (node == null){  //the end node has not been found
                isSearching = false;
                System.out.print("No path exists.");
                done = true;
            }
            else {
                if (node.getState() == NodeState.END) {
                    isSearching = false;
                    isShowingPath = true;
                    target = node;
                    System.out.println("The end node has been found. Showing the path!");
                } else {
                    relax(node);
                }
            }
        }
        else if(isShowingPath){
            if(target.getParent() != null){
                target = target.getParent();
                if(!target.isStartOrEnd())
                target.setState(NodeState.PATH);
            }
            else{
                System.out.println("End of showing the path.");
                isShowingPath = false;
                done = true;
            }
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
