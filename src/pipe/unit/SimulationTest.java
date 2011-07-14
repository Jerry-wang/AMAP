package pipe.unit;

import junit.framework.TestCase;
import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.modules.simulation.*;

public class SimulationTest extends TestCase {

	private Simulation sim;
	
	@Override
	protected void setUp() throws Exception {
		sim = new Simulation();
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		sim = null;
		super.tearDown();
	}
	
	public void testSimulation() {
		
		DataLayerInterface data = new DataLayer("src/pipe/test/resources/ClassicGSPN.xml");
		
		String result = sim.simulate(data, 1, 10);
		System.out.println(result);
		
	}
	
}
