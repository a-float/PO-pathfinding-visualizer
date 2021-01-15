package pathfindingVisualiser;

import java.util.LinkedList;
import java.util.Queue;

public class BfsAlgorithm extends Pathfinder{
    private final Queue<Node> queue = new LinkedList<>();

    public BfsAlgorithm(){
        super(false);
    }

    public void start(Board board){
        super.start(board);
        queue.clear();
        queue.add(startNode);
    }

    private void visit(Node node){
        if(!node.isStartOrEnd()){   //TODO triple isStartOrEnd just like in the DFS
            node.setState(NodeState.VISITED);
        }
        for(Node n: node.getNeighbours()){
            if(n.getState() != NodeState.VISITED && n.getState() != NodeState.BUSY){
                if(!n.isStartOrEnd()) {
                    n.setState(NodeState.BUSY);
                }
                n.setParent(node);
                n.setDistance(node.getDistance()+1);
                queue.add(n);
            }
        }
    }

    @Override
    void search() {
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
