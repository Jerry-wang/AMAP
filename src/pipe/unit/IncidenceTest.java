package pipe.unit;

import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.modules.matrices.Matrices;
import junit.framework.TestCase;

public class IncidenceTest extends TestCase {

	private Matrices incidence;

	private String expectedForwards =   "<table border=0 cellspacing=2><tr class=even><td class=empty></td>" +
										"<td class=colhead>T0</td><td class=colhead>T1</td><td class=colhead>T2</td></tr>" +
										"<tr class=odd><td class=rowhead>P0</td><td class=cell>0</td><td class=cell>0</td>" +
										"<td class=cell>1</td></tr><tr class=even><td class=rowhead>P1</td><td class=cell>1</td>" +
										"<td class=cell>0</td><td class=cell>0</td></tr><tr class=odd><td class=rowhead>P2</td>" +
										"<td class=cell>0</td><td class=cell>1</td><td class=cell>0</td></tr></table>";
	
	private String expectedBackwards =  "<table border=0 cellspacing=2><tr class=even><td class=empty></td>" +
										"<td class=colhead>T0</td><td class=colhead>T1</td><td class=colhead>T2</td></tr>" +
										"<tr class=odd><td class=rowhead>P0</td><td class=cell>1</td><td class=cell>0</td>" +
										"<td class=cell>0</td></tr><tr class=even><td class=rowhead>P1</td><td class=cell>0</td>" +
										"<td class=cell>1</td><td class=cell>0</td></tr><tr class=odd><td class=rowhead>P2</td>" +
										"<td class=cell>0</td><td class=cell>0</td><td class=cell>1</td></tr></table>";
	
	public IncidenceTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		incidence = new Matrices();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		incidence = null;
	}
	
	public void testMatrices() {
		
		DataLayerInterface data = new DataLayer(TestConstants.classicGspn);
		
		String forwardResult = incidence.renderMatrix(data, data.getActiveTokenClass().getForwardsIncidenceMatrix(data.getArcsArrayList(), data.getTransitionsArrayList(), data.getPlacesArrayList()));
		assertTrue("Forwards incidence matrices do not match", forwardResult.equals(expectedForwards));
		
		String backwardsResult = incidence.renderMatrix(data, data.getActiveTokenClass().getBackwardsIncidenceMatrix(data.getArcsArrayList(), data.getTransitionsArrayList(), data.getPlacesArrayList()));
		assertTrue("Backwards incidence matrices do not match", backwardsResult.equals(expectedBackwards));
		
	}
}
