package md.leonis.assistant.utils.fsm;

import md.leonis.assistant.utils.fsm.state.State;
import md.leonis.assistant.utils.fsm.state.SuperState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FSMTestExceptions {

    private FSM<A, String> a;

    private enum A {
        ONE, TWO, THREE
    }

    @SuppressWarnings("unchecked")
    private <E> FSM<A, E> createA0() {
        return new FSM<>(

                new State<A, E>
                        (A.ONE) {
                    public void handleEvent() {
                        next(A.TWO);
                    }
                },
                new SuperState<>(A.ONE, A.TWO));
    }

    @SuppressWarnings("unchecked")
    private <E> FSM<A, E> createA1() {
        return new FSM<>(

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
                new SuperState<>(A.ONE, A.TWO));
    }

    @Test
    void testException1() {
        assertThrows(FSMException.class, () -> a = createA0());
    }

    @Test
    void testException2() {
        assertThrows(FSMException.class, () -> {
            a = createA1();
            a.handleEvent();
            a.handleEvent();
        });
    }
}
