package svg.gui;

/**
 * Class to determine the current state of the machine when an action is performed
 * @author Ivan Guerrero
 */
public class GUIStateMachine {
    public enum ACTION {Q, MOVE, MIRROR, MOUSE_CLICK, ACTION_PERFORMED};
    public enum STATE {SELECT, SET_LOCATION, NEW_LOCATION, PERFORM_MIRROR, PERFORM_MOVE, PERFORM_INSERT};
    private static STATE currentState = STATE.SELECT;
    
    public static STATE getCurrentState() {
        return currentState;
    }
    
    public static void restart() {
        currentState = STATE.SELECT;
    }
    
    public static void actionPerformed(ACTION action) {
        //System.out.print(getCurrentState().name() + " -" + action.name() + "-> ");
        
        switch(currentState) {
            case NEW_LOCATION:
                currentState = newLocationState(action);
                break;
            case PERFORM_INSERT:
                currentState = performInsert(action);
                break;
            case PERFORM_MIRROR:
                currentState = performMirror(action);
                break;
            case PERFORM_MOVE:
                currentState = performMove(action);
                break;
            case SELECT:
                currentState = select(action);
                break;
            case SET_LOCATION:
                currentState = setLocation(action);
                break;
        }
        //System.out.println(getCurrentState().name());
    }
    
    private static STATE newLocationState(ACTION action) {
        STATE state = currentState;
        switch (action) {
            case Q:
                break;
            case MOVE:
                state = STATE.SELECT;
                break;
            case MIRROR:
                break;
            case MOUSE_CLICK:
                state = STATE.PERFORM_MOVE;
                break;
        }
        return state;
    }
    
    private static STATE performInsert(ACTION action) {
        STATE state = currentState;
        switch (action) {
            case Q:
                break;
            case MOVE:
                break;
            case MIRROR:
                break;
            case MOUSE_CLICK:
                break;
            case ACTION_PERFORMED:
                state = STATE.SET_LOCATION;
                break;
        }
        return state;
    }

    private static STATE performMirror(ACTION action) {
        STATE state = currentState;
        switch (action) {
            case Q:
                break;
            case MOVE:
                break;
            case MIRROR:
                break;
            case MOUSE_CLICK:
                break;
            case ACTION_PERFORMED:
                state = STATE.SELECT;
                break;
        }
        return state;
    }

    private static STATE performMove(ACTION action) {
        STATE state = currentState;
        switch (action) {
            case Q:
                break;
            case MOVE:
                break;
            case MIRROR:
                break;
            case MOUSE_CLICK:
                break;
            case ACTION_PERFORMED:
                state = STATE.NEW_LOCATION;
                break;
        }
        return state;
    }

    private static STATE select(ACTION action) {
        STATE state = currentState;
        switch (action) {
            case Q:
                state = STATE.SET_LOCATION;
                break;
            case MOVE:
                state = STATE.NEW_LOCATION;
                break;
            case MIRROR:
                state = STATE.PERFORM_MIRROR;
                break;
            case MOUSE_CLICK:
                break;
        }
        return state;
    }

    private static STATE setLocation(ACTION action) {
        STATE state = currentState;
        switch (action) {
            case Q:
                state = STATE.SELECT;
                break;
            case MOVE:
                break;
            case MIRROR:
                break;
            case MOUSE_CLICK:
                state = STATE.PERFORM_INSERT;
                break;
        }
        return state;
    }
}