package pathfindingVisualiser;

import java.util.*;

public class RecursiveMazeGenerator extends MazeGenerator{
    private final Queue<Vector2> wallsToBulid = new LinkedList<>();

    /**
     * Checks if there is a passage at sepcified psoition
     * @param pos the position to check
     * @return true if passage
     */
    private boolean isPassage(Vector2 pos){
        if(!board.isInBounds(pos)){
            return false;   //out of bounds -> no passage
        }
        else{
            //there should be a wall, but there isn't so it must be a purposefully left passage
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
//        System.out.println("Can this wall be bulid?");
//        System.out.println("start, diff, length "+wallStart+" "+ diff+" "+ wallLength);
        Vector2 posBeforeWall = Vector2.subtract(wallStart, diff);
        Vector2 posAfterWall = new Vector2(wallStart.getX() + wallLength * diff.getX(),
                                           wallStart.getY() + wallLength * diff.getY());
//        System.out.println("pos before and after "+posBeforeWall + " " + posAfterWall);
//        System.out.println("isPassage before and after "+isPassage(posBeforeWall) + " "+ isPassage(posAfterWall));
        return !isPassage(posBeforeWall) && !isPassage(posAfterWall);
    }

    /**
     * Adds positions of soon to be walls to wallToBulid queue
     * @param wallStart position to start buliding a wall form
     * @param diff  buliding direction
     * @param wallLength wall length
     */
    private void bulidWall(Vector2 wallStart, Vector2 diff, int wallLength){
        int passageIndex = VisualizationManager.random.nextInt(wallLength);
        Vector2 currPos = wallStart;
        for(int i = 0; i < wallLength; i++){
            if(i != passageIndex){
                wallsToBulid.add(currPos.copy());
            }
            currPos.add(diff);
        }
    }
    private void divideArea(Vector2 topLeft, Vector2 bottomRight, boolean isVertical){
//        System.out.println("top left, bottom right, isVertical "+isVertical+" "+topLeft+" "+bottomRight);
        int width = bottomRight.getX() - topLeft.getX()+1;
        int height = bottomRight.getY() - topLeft.getY()+1;
        int perpendicularSize, parallelSize;
        Vector2 dir;
        if(isVertical) {
            perpendicularSize = width;
            parallelSize = height;
            dir = Vector2.up();
        }
        else{   //horizontal
            perpendicularSize = height;
            parallelSize = width;
            dir = Vector2.right();
        }

        boolean failedToBulidTheWall = false;   //tried to build a wall but failed because of the passage

        if(perpendicularSize >= 3 && parallelSize > 1){ //enough space to create a wall
            int wallPosIndex = VisualizationManager.random.nextInt(perpendicularSize-2)+1; //no wall at the edge of the area
            Vector2 wallStartPos = new Vector2(topLeft.getX() + dir.getY() * wallPosIndex,
                                               topLeft.getY() + dir.getX() * wallPosIndex);

            if(canBulidWall(wallStartPos, dir, parallelSize)) {
//                System.out.println("can bulid");
                bulidWall(wallStartPos, dir, parallelSize);

                Vector2 newTopLeft1, newBottomRight1, newTopLeft2, newBottomRight2;

                if(isVertical) {
                    newBottomRight1 = Vector2.add(topLeft, new Vector2(wallPosIndex - 1, height - 1));
                    newTopLeft2 = Vector2.add(topLeft, new Vector2(wallPosIndex + 1, 0));
                }
                else {
                    newBottomRight1 = Vector2.add(topLeft, new Vector2(width - 1, wallPosIndex - 1));
                    newTopLeft2 = Vector2.add(topLeft, new Vector2(0, wallPosIndex + 1));
                }
                divideArea(topLeft, newBottomRight1, !isVertical);
                divideArea(newTopLeft2, bottomRight, !isVertical);
            }
            else if(perpendicularSize > 4){    //can't build wall because of a passage
                //an area of perpendicular size of 3 or 4 can have all possible walls blocked by passages
                //just leave it
                //else try again to get a wall not on a passage
                divideArea(topLeft, bottomRight, isVertical);
            }
            else if(parallelSize > 4){ //avoid deadly 4x4 squares with walls full of passages
                    divideArea(topLeft, bottomRight, !isVertical);
            }
        }
        else if(parallelSize >= 3 && perpendicularSize > 1){ //cant this way because of the passage, but maybe the other one can
            divideArea(topLeft, bottomRight, !isVertical);
        }
    }

    @Override
    public void start(Board board, Node startNode) {
        super.start(board,startNode);
        wallsToBulid.clear();
        divideArea(Vector2.zero(), new Vector2(board.getWidth()-1, board.getHeight()-1), true);
    }

    @Override
    protected void mazeStep() {
        if(!wallsToBulid.isEmpty()){
            Vector2 pos = wallsToBulid.poll();
            Node node = board.getNodeAt(pos);
            node.setState(NodeState.WALL);
        }
        else{
            done = true;
        }
    }
}
