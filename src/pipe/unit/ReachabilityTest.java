package pipe.unit;

import java.io.File;
import java.io.IOException;

import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.calculations.StateSpaceGenerator;
import pipe.common.dataLayer.calculations.TimelessTrapException;
import pipe.io.ImmediateAbortException;
import junit.framework.TestCase;

public class ReachabilityTest extends TestCase {
	
	public void testRGFileGen() {
		
		//Create a DataLayer object and analysis file
		DataLayerInterface data = new DataLayer(TestConstants.timelessTrap);
		File reachabilityGraph = new File("results.rg");
		
		try {
			//Attempt generation of a reachability graph
			StateSpaceGenerator.generate(data, reachabilityGraph);
		}
		catch (Exception e){
			//Exceptions not handled in test case
		}
		
		assertTrue("Failed to create analysis file", reachabilityGraph.exists());	
	}
}
