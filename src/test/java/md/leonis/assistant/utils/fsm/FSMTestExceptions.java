package md.leonis.assistant.utils.fsm;

import md.leonis.assistant.utils.fsm.state.State;
import md.leonis.assistant.utils.fsm.state.SuperState;
import org.junit.Test;

public class FSMTestExceptions {
	private FSM<A, String> a;

	private enum A {
		ONE, TWO, THREE
	};

	@SuppressWarnings("unchecked")
	private <E extends Object> FSM<A, E> createA0() {
		return new FSM<A, E>(

		new State<A, E>
		(A.ONE) {
			public void handleEvent() { next(A.TWO); }
		},
		new SuperState<A, E>
		(A.ONE, A.TWO)
		);
	}

	@SuppressWarnings("unchecked")
	private <E extends Object> FSM<A, E> createA1() {
		return new FSM<A, E>(

		new State<A, E>
		(A.ONE) {
			public void handleEvent() { next(A.TWO); }
		},
		new State<A, E>
		(A.TWO) {
			public void handleEvent() { next(A.THREE); }
		},
		new SuperState<A, E>
		(A.ONE, A.TWO)
		);
	}

	@Test(expected = FSMException.class)
	public void testException1() {
		a = createA0();
	}
	
	@Test(expected = FSMException.class)
	public void testException2() {
		a = createA1();
		a.handleEvent();
		a.handleEvent();
	}
}
