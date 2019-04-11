package md.leonis.assistant.utils.fsm.state;

import md.leonis.assistant.utils.fsm.FSM;

import java.util.ArrayList;
import java.util.List;

public class State<STATES extends Enum<?>, EVENT> implements IState {

    // {{ Properties

    private STATES id;

    public STATES getId() {
        return id;
    }

    protected void setId(final STATES id) {
        this.id = id;
    }

    private STATES nextState;

    public STATES getNextState() {
        return nextState;
    }

    protected void setNextState(final STATES nextStateId) {
        nextState = nextStateId;
    }

    public void clearNextState() {
        setNextState(null);
    }

    // }}

    // {{ Dependencies

    public void validate() {
        assert (fsm != null);
    }

    private FSM<STATES, EVENT> fsm;

    public FSM<STATES, EVENT> getFsm() {
        return fsm;
    }

    public void setFsm(final FSM<STATES, EVENT> fsm) {
        this.fsm = fsm;
    }

    private List<SuperState<STATES, EVENT>> superStates;

    public List<SuperState<STATES, EVENT>> getSuperStates() {
        return superStates;
    }

    public void addSuperState(SuperState<STATES, EVENT> superState) {
        if (superStates == null)
            superStates = new ArrayList<>(1);
        this.superStates.add(superState);
    }

    // }}

    public State(final STATES id) {
        super();
        this.id = id;
    }

    public EVENT e() {
        return getFsm().getEvent();
    }

    public void next(final STATES nextStateId) {
        setNextState(nextStateId);
    }

    public void handleEventInternal() {
        handleEvent();
    }

    public void handleEvent() {
    }

    public void exitInternal() {
        exit();
    }

    public void enterInternal() {
        enter();
    }

    public void exit() {
    }

    public void enter() {
    }

    @Override
    public String toString() {
        return id.name() + "[" + id.ordinal() + "]";
    }
}
