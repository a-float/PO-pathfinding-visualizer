package pathfindingVisualiser;

import java.util.LinkedList;
import java.util.Queue;

public class BfsAlgorithm extends Pathfinder{
    private final Queue<Node> queue = new LinkedList<>();

    public BfsAlgorithm(){
        super(false);
    }

    public void start(Board board, Node startNode){
        super.start(board, startNode);
        queue.clear();
        queue.add(startNode);
    }

    /**
     * Adds all neighbours neighbours to the queue. Sets its state to visited.
     * @param node node to visit
     */
    private void visit(Node node){
        node.trySetState(NodeState.VISITED);
        for(Node n: node.getNeighbours()){
            if(n.getState() != NodeState.VISITED && n.getState() != NodeState.BUSY){
                n.trySetState(NodeState.BUSY);
                n.setParent(node);
                n.setDistance(node.getDistance()+1);
                queue.add(n);
            }
        }
    }

    @Override
    protected void search() {
        if(!queue.isEmpty()){
            Node nodeToVisit = queue.poll();
            if(nodeToVisit.getState() == NodeState.END){
                finishSearch(nodeToVisit);
            }
            else{
                visit(nodeToVisit);
            }
        }
        else{
            abandonSearch();
        }
    }
}
