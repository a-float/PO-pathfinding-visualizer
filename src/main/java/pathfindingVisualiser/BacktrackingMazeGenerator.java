package pathfindingVisualiser;

import java.util.*;

public class BacktrackingMazeGenerator implements BoardEditor{
    private boolean done;
    private final Stack<Node> stack = new Stack<>();
    private Board board;
    private final Random random = new Random();

    private ArrayList<Node> getFarNeighbours(Node node){
        ArrayList<Node> result = new ArrayList<>(4);
        Vector2 newVec;
        Vector2 pos = node.getPosition();
        for(int dx = -2; dx <= 2; dx+=2){
            for(int dy = -2; dy <= 2; dy+=2){
                if(dx == 0 || dy == 0) { newVec = new Vector2(pos.getX() + dx, pos.getY() + dy);
                   if(board.isInRange(newVec) && board.getNodeAt(newVec).getState() != NodeState.WALL){
                       result.add(board.getNodeAt(newVec));
                   }
                }
            }
        }
        return result;
    }

    private void visit(Node node){
        List<Node> nodes = getFarNeighbours(node);
        Node chosenNode;
        if(nodes.size() > 0){
            chosenNode = nodes.get(random.nextInt(nodes.size()));
        }
    }

    @Override   //TODO add abstract class here as well probably
    public void start(Board board) {
        stack.clear();
        done = false;
        this.board = board;
    }

    @Override
    public void step() {

    }

    @Override
    public boolean isDone() {
        return false;
    }
}
