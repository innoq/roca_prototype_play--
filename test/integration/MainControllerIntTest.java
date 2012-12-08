package integration;

import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;

import org.junit.Test;

import play.libs.WS;

public class MainControllerIntTest extends PlayBaseTest {

	private static final int PORT = 3333;
	private static final String HTTP_LOCALHOST_PORT = "http://localhost:" + PORT;
	private static final String ISSUES_ROOT_URI = HTTP_LOCALHOST_PORT + "/services/issues";

	@Test
	public void entryUrlIsReachable() {

		assertTrue(ISSUES_ROOT_URI + " was not reachable", WS.url(ISSUES_ROOT_URI).get().get().getStatus() == OK);
	}

}
