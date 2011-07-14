package pipe.unit;

import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.modules.comparison.Comparison;
import junit.framework.*;

public class ComparisonTest extends TestCase {

	private Comparison comp;
	private String expected = "<h2>Places</h2><table border=0 cellspacing=2>" +
								"<tr class=even><td class=colhead>Source place name</td>" +
								"<td class=colhead>Comparison place name</td><td class=colhead>" +
								"Comparison</td></tr><tr class=odd><td class=cell>P0</td><td class=cell>P0</td>" +
								"<td class=cell>Identical</td></tr><tr class=even><td class=cell>P1</td>" +
								"<td class=cell>P1</td><td class=cell>Identical</td></tr><tr class=odd>" +
								"<td class=cell>P2</td><td class=cell>P2</td><td class=cell>Identical</td></tr>" +
								"</table>";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		comp = new Comparison();
	}
	
	@Override
	protected void tearDown() throws Exception {
		comp = null;
		super.tearDown();
	}
	
	public void testComparison() {
		
		DataLayerInterface first = new DataLayer("src/pipe/test/resources/ClassicGSPN.xml");
		DataLayerInterface second = new DataLayer("src/pipe/test/resources/ClassicGSPN.xml");
		
		String result = comp.comparePlaces(first.getPlaces(), second.getPlaces(), true, true, true, true);
		
		assertTrue("The comparison indicates that petri nets are not the same, this is incorrect", expected.equals(result));
		
	}
	
}
