package integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = {IssueModelIntTest.class, MainControllerIntTest.class
})
public class IntTests {

}
