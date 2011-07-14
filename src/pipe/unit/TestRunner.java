package pipe.unit;

import junit.framework.Test;
import junit.framework.TestSuite;


public class TestRunner {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for pipe");
		//$JUnit-BEGIN$
		suite.addTestSuite(IncidenceTest.class);
		suite.addTestSuite(GspnTest.class);
		suite.addTestSuite(ClassificationTest.class);
		suite.addTestSuite(ComparisonTest.class);
		suite.addTestSuite(SimulationTest.class);
		suite.addTestSuite(StateSpaceTest.class);
		//$JUnit-END$
		return suite;
	}

}
