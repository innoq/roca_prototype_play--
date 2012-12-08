package integration;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import play.test.FakeApplication;
import play.test.Helpers;
import play.test.TestServer;

import com.avaje.ebean.Ebean;

public class PlayBaseTest {

	static TestServer testServer;

	@BeforeClass
	public static void setup() {
		FakeApplication app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
		testServer = Helpers.testServer(3333, app);
		testServer.start();

		Ebean.save(TestUtil.TEST_USER);
	}

	@AfterClass
	public static void stopApp() {
		Helpers.stop(testServer);
	}

}
