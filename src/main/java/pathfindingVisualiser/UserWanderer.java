package pathfindingVisualiser;

import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class UserWanderer {
    private final Stack<Node> nodeHistory = new Stack<>();
    private final Stack<Direction> moveHistory = new Stack<>();
    private final HashMap<Node, Integer> nodeVisitations = new HashMap<>();
    private Board board;

    private final Map<String, Direction> keyboardControls = Map.of(
            "w", Direction.UP,
            "d", Direction.RIGHT,
            "s", Direction.DOWN,
            "a", Direction.LEFT
    );

    public void setBoard(Board board){
        this.board = board;
        this.nodeHistory.push(board.getStartNode());
    }

    public void reset() {
        clearPath();
        moveHistory.clear();
        nodeHistory.clear();
        nodeVisitations.clear();
        if(this.board != null) nodeHistory.push(board.getStartNode());
    }

    public void clearPath() {
        for(Node node: nodeHistory){
            node.setPartOfWandererPath(false);
        }
    }

    private enum Direction{
        UP, DOWN, LEFT, RIGHT;
        Direction opposite(){
            return switch(this){
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }

        public Vector2 toVector2() {
            return switch(this){
                case UP -> Vector2.down();
                case DOWN -> Vector2.up();
                case LEFT -> Vector2.left();
                case RIGHT -> Vector2.right();
            };
        }
    }
    public Node getNodeToStartFrom(){
        return nodeHistory.peek();
    }

    public boolean handleKey(KeyEvent key) {
        if(keyboardControls.containsKey(key.getText())){
            return move(keyboardControls.get(key.getText()));
        }
        return false;
    }

    /**
     * @param direction Direction the user wants to move in
     * @return true if the board may have changed and needs a redraw
     */
    private boolean move(Direction direction) {
        if(!moveHistory.isEmpty() && nodeHistory.peek().getState() != NodeState.START
                                  && moveHistory.peek() == direction.opposite()){ //backtracking eg. LEFT then RIGHT
            moveHistory.pop();
            removeFromPath(nodeHistory.pop());
            return true;
        }
        else{
            Vector2 targetPosition = Vector2.add(nodeHistory.peek().getPosition(), direction.toVector2());
            if(board.isInBounds(targetPosition)) {
                Node targetNode = board.getNodeAt(targetPosition);
                if(targetNode.getState() != NodeState.WALL && !targetNode.isStartOrEnd()){
                    nodeHistory.push(targetNode);
                    moveHistory.add(direction);
                    addToPath(targetNode);
                    return true;
                }
            }
        }
        return false;
    }

    private void addToPath(Node node){
        if(!nodeVisitations.containsKey(node)){
            nodeVisitations.put(node, 1);
            node.setPartOfWandererPath(true);
        }
        else nodeVisitations.put(node, nodeVisitations.get(node)+1);
    }

    private void removeFromPath(Node node){
        if(nodeVisitations.get(node) == 1){
            nodeVisitations.remove(node);
            node.setPartOfWandererPath(false);
        }
        else nodeVisitations.put(node, nodeVisitations.get(node)-1);
    }
}
