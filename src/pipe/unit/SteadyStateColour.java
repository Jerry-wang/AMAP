package pipe.unit;

import junit.framework.*;
import static org.mockito.Mockito.*;

import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.gui.widgets.ResultsHTMLPane;
import pipe.modules.clientCommon.HTMLPane;
import pipe.modules.steadyState.Analyse;

public class SteadyStateColour extends TestCase {

	@Override
	protected void setUp() throws Exception {
	
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
	
		super.tearDown();
	}
	
	public void testColouredInput() {
		
		// don't care about results
		HTMLPane mockPane = mock(HTMLPane.class);
		ResultsHTMLPane mockResults = mock(ResultsHTMLPane.class);
		
		DataLayerInterface data = new DataLayer(TestConstants.simpleColour);
		
		// construct analyser
		Analyse analyser = new Analyse(data, mockPane, mockResults);
	
		// assigning back to datalayer object
		data = analyser.checkColour(data);
		
		// unfolder should result in one token class
		assertEquals(1, data.getTokenClasses().size());
		
		}
	
	
}
