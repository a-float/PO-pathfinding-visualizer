package pathfindingVisualiser;

import java.util.PriorityQueue;

public class DijkstraAlgorithm extends Pathfinder{
    private final PriorityQueue<Node> queue = new PriorityQueue<>();

    public DijkstraAlgorithm(){
        super(true);
    }

    @Override
    public void start(Board board) {    //TODO doesnt actually need the board, maybe no pathfinder does
        super.start(board);
        queue.clear();
        relax(startNode);
    }

    @Override
    void search() {
        Node node = queue.poll(); //returns null when queue empty
        if (node == null){  //the end node has not been found
            abandonSearch();
        }
        else {
            if (node.getState() == NodeState.END) {
                finishSearch(node);
            } else {
                relax(node);
            }
        }
    }

    private void relax(Node relaxNode){
        for(Node node: relaxNode.getNeighbours()){
            double newDistance = relaxNode.getDistance() + node.getWeight();
            if(newDistance < node.getDistance()) {
                node.setParent(relaxNode);
                node.setDistance(newDistance);
                node.trySetState(NodeState.BUSY);
                queue.add(node);
            }
        }
        relaxNode.trySetState(NodeState.VISITED);
    }
}
