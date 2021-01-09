package pathfindingVisualiser;

//TODO possibly remove the isWallable thing
public enum NodeState {
    WALL, FREE, VISITED, BUSY, START, END;  //TODO change BUSY to something more descriptive?

//    private static NodeState[] wallable = new NodeState[] {FREE}
    public String toString(){
        return switch(this){
            case WALL -> "X";
            case FREE -> ".";
            case VISITED -> "o";
            case BUSY -> "+";
            case START -> "*";
            case END -> "v";
        };
    }
//    public boolean isWallable(){
//        if(this == )
//    }
}
