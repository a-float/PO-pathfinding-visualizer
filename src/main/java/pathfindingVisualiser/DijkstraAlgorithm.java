package pathfindingVisualiser;

import java.util.PriorityQueue;

public class DijkstraAlgorithm extends Pathfinder{
    private final PriorityQueue<Node> queue = new PriorityQueue<>();

    public DijkstraAlgorithm(){
        super(true);
    }

    @Override
    public void start(Board board, Node startNode) {
        super.start(board, startNode);
        queue.clear();
        relax(startNode);
    }

    @Override
    protected void search() {
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

    /**
     * If the path to relaxNode neighbours is shorter then the previously found one. Updates it,
     * then sets relaxNode's state to visited.
     * @param relaxNode node to relax
     */
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
