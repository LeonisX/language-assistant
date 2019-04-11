package md.leonis.assistant.utils.fsm;

import md.leonis.assistant.utils.fsm.state.State;
import md.leonis.assistant.utils.fsm.state.SuperState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FSMTestElementary {

    private FSM<A, String> a;

    private enum A {
        ONE, TWO
    }

    @SuppressWarnings("unchecked")
    private <E> FSM<A, E> createA0() {
        return new FSM<>(

                new State<A, E>
                        (A.ONE) {
                    public void enter() {
                        log("enter1");
                    }

                    public void handleEvent() {
                        log("handle1");
                        if (e().equals("E1")) next(A.TWO);
                    }

                    public void exit() {
                        log("exit1");
                    }
                },
                new State<A, E>
                        (A.TWO) {
                    public void enter() {
                        log("enter2");
                    }

                    public void handleEvent() {
                        log("handle2");
                        if (e().equals("E1")) next(A.ONE);
                    }

                    public void exit() {
                        log("exit2");
                    }
                },
                new SuperState<>(A.ONE, A.TWO)
        );
    }

    private String log;

    @BeforeEach
    void setUp() {
        log = "";
        a = createA0();
    }

    @Test
    void testFSM() {
        assertEquals(a.getState(A.ONE), a.getCurrentState());
        assertNull(a.getEvent());
        assertNull(a.getPrevState());
        assertEquals(a.getCurrentState().toString(), a.toString());
        assertEquals(A.ONE, a.y());

        a.setEvent("E1");
        assertEquals("E1", a.getEvent());
        a.handleEvent();
        assertEquals(a.getState(A.TWO), a.getCurrentState());
        assertNull(a.getEvent());
        assertEquals(a.getState(A.ONE), a.getPrevState());

        assertEquals(a, a.getCurrentState().getFsm());
        assertTrue(a.getCurrentState().getSuperStates().get(0).toString().contains("ONE"));
        assertTrue(a.getCurrentState().getSuperStates().get(0).toString().contains("TWO"));
        assertTrue(a.getCurrentState().getSuperStates().get(0).getIncluded().contains(A.ONE));
        assertTrue(a.getCurrentState().getSuperStates().get(0).getIncluded().contains(A.TWO));
        assertEquals("TWO", a.getCurrentState().getId().name());
        assertEquals(A.TWO, a.y());
    }

    private void log(final String msg) {
        if (log != null && log.length() > 0)
            log += "-";
        log += msg;
    }
}
