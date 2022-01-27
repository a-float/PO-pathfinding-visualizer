package me.board;

import javafx.scene.paint.Color;

import java.util.Map;

public enum NodeState {
    WALL, FREE, VISITED, BUSY, START, END, PATH, WANDERER_PATH;

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

    public static final Map<NodeState, Color> legendColorMap;
    static {
        legendColorMap = Map.of(
                NodeState.FREE, Color.HOTPINK,
                NodeState.START, Color.GREEN,
                NodeState.END, Color.ORANGERED,
                NodeState.WALL, Color.BLACK,
                NodeState.VISITED, Color.DARKBLUE,
                NodeState.BUSY, Color.CYAN,
                NodeState.PATH, Color.YELLOW,
                NodeState.WANDERER_PATH, Color.LIGHTGREEN);
    }
    public static final Map<NodeState, String> legendStateDescriptionMap;
    static {
        legendStateDescriptionMap = Map.of(
                NodeState.FREE, "A cell that has not been visited yet. It is white if\nnode's weights is 1, and the more pink the bigger weight\nit has",
                NodeState.START, "The cell from which searching and wandering starts off.",
                NodeState.END, "The search target cell.",
                NodeState.WALL, "A wall, an immovable obstacle.",
                NodeState.VISITED, "A cell that has already been visited by the\nalgorithm, and will never be revisited.",
                NodeState.BUSY, "A cell that the pathfinding algorithm has taken\ninto consideration but hasn't visited yet.",
                NodeState.PATH, "Cell included in the found path.",
                NodeState.WANDERER_PATH, "Path the user wanderer has taken.\nPathfinding algorithms start off\nfrom it's end.");
    }
}
