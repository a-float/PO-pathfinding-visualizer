package pathfindingVisualiser;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AstarAlgorithm extends Pathfinder{
    //stores h(n)+g(n) for every busy node. h is the heuristic and g distance from the start.
    private final HashMap<Node, Double> nodeValues = new HashMap<>();
    private Node endNode;
    Comparator<Node> nodeComparator = Comparator
            .comparing(nodeValues::get);

    private final PriorityQueue<Node> queue = new PriorityQueue<>(3, nodeComparator);

    public AstarAlgorithm(){
        super(true);
    }

    @Override
    public void start(Board board, Node startNode) {
        super.start(board, startNode);
        queue.clear();
        nodeValues.clear();
        endNode = board.getEndNode();
        relax(startNode);
    }

    @Override
    protected void search() {
        Node node = queue.poll(); //returns null when the queue is empty
        if (node == null){  //the end node has not been found
            abandonSearch();
        }
        else {
            if (node.getState() == NodeState.END) {
               finishSearch(node);
            }
            else if(node.getState() != NodeState.VISITED){
                relax(node);
            }
        }
    }

    /**
     * @param a start node
     * @return manhattan distance between endNode and startNodes' positions
     */
    private int manhattanDistFromEnd(Node a){
        Vector2 diff = Vector2.subtract(endNode.getPosition(), a.getPosition());
        return diff.getX()*diff.getX() + diff.getY()*diff.getY();
    }

    /**
     * Updates distances to all of relaxNode neighbours. Possibly adds them to the queue again.
     * @param relaxNode the node to be relaxed. Relaxed nodes become visited.
     */
    private void relax(Node relaxNode){
        for(Node node: relaxNode.getNeighbours()){
            double newDistance = relaxNode.getDistance() + node.getWeight();
            if(newDistance < node.getDistance()) {
                node.setParent(relaxNode);
                node.setDistance(newDistance);
                node.trySetState(NodeState.BUSY);
                nodeValues.put(node, node.getDistance()+manhattanDistFromEnd(node));
                queue.add(node);
            }
        }
        relaxNode.trySetState(NodeState.VISITED);
    }
}
