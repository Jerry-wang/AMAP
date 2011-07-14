package pipe.unit;

import java.io.File;
import junit.framework.*;
import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.PNMatrix;
import pipe.gui.CreateGui;
import pipe.gui.GuiFrame;
import pipe.gui.GuiView;
import pipe.modules.invariantAnalysis.InvariantAnalysis;
import pipe.modules.stateSpace.*;

public class StateSpaceTest extends TestCase {

	private StateSpace stateSpace;
	private boolean[] expected = {true,false,false};
	
	public StateSpaceTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		stateSpace = new StateSpace();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		stateSpace = null;
	}
	
	public void testStateSpace() throws Exception{
		
		DataLayerInterface data = new DataLayer(TestConstants.classicGspn);
		boolean ss[] = stateSpace.getStateSpace(data);
		boolean same = true;
		
		for (int i = 0; i<ss.length; i++){
			if(ss[i]!=expected[i]) same = true;
		}
		
		assertTrue("State Space array values unexpected", same);
		
	}
	
}
