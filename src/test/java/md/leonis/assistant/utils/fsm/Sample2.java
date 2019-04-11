package md.leonis.assistant.utils.fsm;

import md.leonis.assistant.utils.fsm.state.State;
import md.leonis.assistant.utils.fsm.state.SuperState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Sample2 {
	
	private FSM<A, String> a;

	private enum A {
		INIT, SHOW, SAVE, HIDE
	}

	@SuppressWarnings("unchecked")
	private <E> FSM<A, E> createA() {
		return new FSM<>(

		new State<A, E>
		(A.INIT) {
			public void enter()       { init();                       next(A.SHOW); }
		}, 
		new State<A, E>
		(A.SHOW) {
			public void enter()       { show(); }
			public void handleEvent() { if( e().equals("SaveEvent") ) next(A.SAVE); }
		},
		new State<A, E>
		(A.SAVE) {
			public void enter()       { save();                       next(A.HIDE); }
		},
		new State<A, E>
		(A.HIDE) {
			public void enter()       { hide(); }
			public void handleEvent() { if( e().equals("ShowEvent") ) next(A.SHOW); }
		}, 
		new SuperState<A, E>
		(A.SHOW, A.SAVE) {
			public void handleEvent() { if( e().equals("HideEvent") ) next(A.HIDE); }
		}
		);
	}

	@Before
	public void setUp() {
		log = "";
		a = createA();
	}

	private String log;

	@Test
	public void testSample() {
		a.handleEvent("SaveEvent");

		a.handleEvent("ShowEvent");

		a.handleEvent("HideEvent");

		Assert.assertEquals("init-show-save-hide-show-hide", log);
	}

	private void init() {
		log("init");
	}

	private void show() {
		log("show");
	}

	private void hide() {
		log("hide");
	}

	private void save() {
		log("save");
	}

	private void log(String msg) {
		if (log != null && log.length() > 0)
			log += "-";
		log += msg;
	}
}
