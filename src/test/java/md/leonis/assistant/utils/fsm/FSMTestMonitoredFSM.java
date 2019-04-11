package md.leonis.assistant.utils.fsm;

import java.util.EnumSet;

import md.leonis.assistant.utils.fsm.state.State;
import md.leonis.assistant.utils.fsm.state.SuperState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FSMTestMonitoredFSM {
    private MonitoredFSM<A, String> a;

    private enum A {
        ONE, TWO, THREE
    }

    @SuppressWarnings("unchecked")
    private <E> MonitoredFSM<A, E> createA0() {
        return new MonitoredFSM<>(

                new State<A, E>
                        (A.ONE) {
                    public void handleEvent() {
                        next(A.TWO);
                    }
                },
                new State<A, E>
                        (A.TWO) {
                    public void handleEvent() {
                        next(A.THREE);
                    }
                },
                new State<A, E>
                        (A.THREE) {
                    public void handleEvent() {
                        next(A.ONE);
                    }
                },
                new SuperState<>(A.ONE, A.TWO),
                new SuperState<>(A.TWO, A.THREE)
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
        MonitoredFSM.Monitor mon;
        mon = (fsm, e, state) -> {
            String logMsg = e.name();
            if (state != null) {
                logMsg += "(" + state.toString() + ")";
            }
            log(logMsg);
        };
        a.addMonitor(mon, EnumSet.allOf(FSM.FsmEvents.class));

        a.handleEvent();
        assertEquals("BEFORE_HANDLE_EVENT"
                + "-BEFORE_STATE_HANDLE_EVENT(SuperState for [ONE, TWO])"
                + "-AFTER_STATE_HANDLE_EVENT(SuperState for [ONE, TWO])"
                + "-BEFORE_STATE_HANDLE_EVENT(ONE[0])"
                + "-AFTER_STATE_HANDLE_EVENT(ONE[0])"
                + "-BEFORE_TRANSITION"
                + "-BEFORE_STATE_EXIT(ONE[0])"
                + "-AFTER_STATE_EXIT(ONE[0])"
                + "-BEFORE_SWITCH_STATE"
                + "-AFTER_SWITCH_STATE"
                + "-BEFORE_STATE_ENTER(SuperState for [TWO, THREE])"
                + "-AFTER_STATE_ENTER(SuperState for [TWO, THREE])"
                + "-BEFORE_STATE_ENTER(TWO[1])"
                + "-AFTER_STATE_ENTER(TWO[1])"
                + "-AFTER_TRANSITION"
                + "-AFTER_HANDLE_EVENT", log);
        log = "";

        a.removeMonitor(mon);
        a.addMonitor(mon, FSM.FsmEvents.AFTER_TRANSITION,
                FSM.FsmEvents.BEFORE_STATE_ENTER,
                FSM.FsmEvents.BEFORE_STATE_EXIT);
        a.handleEvent();
        assertEquals("BEFORE_STATE_EXIT(TWO[1])"
                + "-BEFORE_STATE_EXIT(SuperState for [ONE, TWO])"
                + "-BEFORE_STATE_ENTER(THREE[2])-AFTER_TRANSITION", log);
        log = "";
        a.handleEvent();
        assertEquals("BEFORE_STATE_EXIT(THREE[2])"
                + "-BEFORE_STATE_EXIT(SuperState for [TWO, THREE])"
                + "-BEFORE_STATE_ENTER(SuperState for [ONE, TWO])"
                + "-BEFORE_STATE_ENTER(ONE[0])-AFTER_TRANSITION", log);
        log = "";
    }

    private void log(final String msg) {
        if (log != null && log.length() > 0)
            log += "-";
        log += msg;
    }
}
