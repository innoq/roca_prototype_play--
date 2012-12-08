package integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ IssueModelIntTest.class, MainControllerIntTest.class,
		UserModelIntTest.class })
public class IntTests {

}
