package pathfindingVisualiser;

import javafx.scene.paint.Color;

import java.util.Map;

//TODO possibly remove the isWallable thing
public enum NodeState {
    WALL, FREE, VISITED, BUSY, START, END, PATH, WANDERER_PATH;  //TODO change BUSY to something more descriptive?

    //    private static NodeState[] wallable = new NodeState[] {FREE}
    public String toString(){
        return switch(this){
            case WALL -> "WALL";
            case FREE -> "FREE";
            case VISITED -> "VISITED";
            case BUSY -> "BUSY";
            case START -> "START";
            case END -> "END";
            case PATH -> "PATH";
            case WANDERER_PATH -> "WANDERER_PATH";
        };
    }

    static final Map<NodeState, Color> colorMap;
    static {
        colorMap = Map.of(
                NodeState.FREE, Color.HOTPINK,
                NodeState.START, Color.GREEN,
                NodeState.END, Color.ORANGERED,
                NodeState.WALL, Color.BLACK,
                NodeState.VISITED, Color.DARKBLUE,
                NodeState.BUSY, Color.CYAN,
                NodeState.PATH, Color.YELLOW,
                NodeState.WANDERER_PATH, Color.LIGHTGREEN);
    }
}
