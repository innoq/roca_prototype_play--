package suite;

import integration.IntTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import controllers.UnitTests;

@RunWith(Suite.class)
@SuiteClasses({ IntTests.class, UnitTests.class })
public class AllTests {

}
