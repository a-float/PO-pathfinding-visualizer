package pathfindingVisualiser;

import java.util.*;

public class RecursiveMazeGenerator implements BoardEditor{
    private Board board;
    private boolean done;
    private final Queue<Vector2> wallsToBulid = new LinkedList<>();
    private static final Random random = new Random();

    /**
     * Checks if there is a passage at sepcified psoition
     * @param pos the position to check
     * @return true if passage
     */
    private boolean isPassage(Vector2 pos){
        if(pos.getX() < 0 || pos.getX() >= board.getWidth() ||
                pos.getY() < 0 || pos.getY() >= board.getHeight()) {
            return false;   //there is no wall there, so its a passage
        }
        else{
            return !wallsToBulid.contains((pos));
        }
    }

    /**
     * Checks if the wall doesn't block an exisiting passage beofre or after it
     * @param wallStart position to start buliding a wall form
     * @param diff  buliding direction
     * @param wallLength wall length
     * @return true if the wall can be build
     */
    private boolean canBulidWall(Vector2 wallStart, Vector2 diff, int wallLength) {
        Vector2 posBeforeWall = Vector2.subtract(wallStart, diff);
        Vector2 posAfterWall = new Vector2(wallStart.getX() + (wallLength+1) * diff.getX(),
                                           wallStart.getY() + (wallLength+1) * diff.getY());
        return !isPassage(posBeforeWall) && !isPassage(posAfterWall);
    }

    /**
     * Adds positions of soon to be walls to wallToBulid queue
     * @param wallStart position to start buliding a wall form
     * @param diff  buliding direction
     * @param wallLength wall length
     */
    private void bulidWall(Vector2 wallStart, Vector2 diff, int wallLength){
        int passageIndex = random.nextInt(wallLength);
        Vector2 currPos = wallStart;
        for(int i = 0; i < wallLength; i++){
            if(i != passageIndex){
                wallsToBulid.add(currPos.copy());
            }
            currPos.add(diff);
        }
    }
    private void divideAreaHorizontally(Vector2 topLeft, Vector2 bottomRight){
        System.out.println("horizotnal "+topLeft+" "+ bottomRight);
        int width = bottomRight.getX() - topLeft.getX()+1;
        int height = bottomRight.getY() - topLeft.getY()+1;
        boolean canWall = true;
        if(height >= 3){ //creating vertical wall
            int wallRow = random.nextInt(height-2)+1; //no wall at the edge of the area
            Vector2 wallStart = Vector2.add(topLeft, new Vector2(0,wallRow));

            if(canBulidWall(wallStart, Vector2.right(), width)) {
                System.out.println("can bulid");
                bulidWall(wallStart, Vector2.right(), width);

                Vector2 newBottomRight1 = Vector2.add(topLeft, new Vector2(width-1, wallRow-1));
                Vector2 newTopLeft2 = Vector2.add(topLeft, new Vector2(0, wallRow+1));

                divideAreaVertically(topLeft, newBottomRight1);
                divideAreaVertically(newTopLeft2, bottomRight);
                return;
            }
            else{
                canWall = false;
            }
        }
        if(width >= 3 || !canWall){ //cant divide horizontally, but can vertically
            divideAreaVertically(topLeft, bottomRight);
        }
    }

    private void divideAreaVertically(Vector2 topLeft, Vector2 bottomRight){
        System.out.println("vertical "+topLeft+" "+ bottomRight);
        int width = bottomRight.getX() - topLeft.getX()+1;
        int height = bottomRight.getY() - topLeft.getY()+1;
        boolean canWall = true;

        if(width >= 3){ //creating vertical wall
            int wallColumn = random.nextInt(width-2)+1; //no wall at the edge of the area
            Vector2 wallStart = Vector2.add(topLeft, new Vector2(wallColumn,0));

            if(canBulidWall(wallStart, Vector2.up(), height)) {
                bulidWall(Vector2.add(topLeft, new Vector2(wallColumn, 0)), Vector2.up(), height);

                Vector2 newBottomRight1 = Vector2.add(topLeft, new Vector2(wallColumn - 1, height - 1));
                Vector2 newTopLeft2 = Vector2.add(topLeft, new Vector2(wallColumn + 1, 0));

                divideAreaHorizontally(topLeft, newBottomRight1);
                divideAreaHorizontally(newTopLeft2, bottomRight);
                return;
            }
            else{
                canWall = false;
            }
        }
        if(height >= 3 || !canWall){ //cant divide horizontally, but can vertically
            divideAreaHorizontally(topLeft, bottomRight);
        }
    }

    @Override
    public void start(Board board) {
        this.board = board;
        done = false;
        wallsToBulid.clear();
        divideAreaHorizontally(Vector2.zero(), new Vector2(board.getWidth()-1, board.getHeight()-1));
        System.out.println(wallsToBulid);
    }

    @Override
    public void step() {
        if(!wallsToBulid.isEmpty()){
            Vector2 pos = wallsToBulid.poll();
            System.out.println(pos);
            Node node = board.getNodeAt(pos);
            node.setState(NodeState.WALL);
        }
        else{
            done = true;
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
