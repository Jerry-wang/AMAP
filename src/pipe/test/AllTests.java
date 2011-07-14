package pipe.test;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for pipe");
		//$JUnit-BEGIN$
		suite.addTestSuite(AnimatorTest.class);
		suite.addTestSuite(GuiFrameTest.class);
		suite.addTestSuite(GuiViewTest.class);
		suite.addTestSuite(InvariantAnalysisTest.class);
		suite.addTestSuite(SaveLoadTest.class);
		suite.addTestSuite(StateRecordTest.class);
		suite.addTestSuite(TransitionRecordTest.class);
		suite.addTestSuite(UnfolderTest.class);
		//$JUnit-END$
		return suite;
	}

}
