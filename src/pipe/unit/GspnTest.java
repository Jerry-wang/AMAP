package pipe.unit;

import java.io.File;
import java.io.IOException;

import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.calculations.StateList;
import pipe.common.dataLayer.calculations.StateSpaceGenerator;
import pipe.common.dataLayer.calculations.StateSpaceTooBigException;
import pipe.common.dataLayer.calculations.SteadyStateSolver;
import pipe.common.dataLayer.calculations.TimelessTrapException;
import pipe.gui.widgets.ResultsHTMLPane;
import pipe.io.ImmediateAbortException;
import pipe.modules.gspn.GSPNNew;
import junit.framework.TestCase;

public class GspnTest extends TestCase {

	private GSPNNew gspn;
	
	private String expectedTangibleStates = "<table border=0 cellspacing=2><tr><td class=colhead>Set of Tangible States</td>" +
											"</tr><tr><td class=cell><table border=0 cellspacing=2><tr class=even><td class=empty></td>" +
											"<td class=colhead>P0</td><td class=colhead>P1</td><td class=colhead>P2</td></tr>" +
											"<tr class=odd><td class=rowhead>M0<A NAME= 'M0'></A></td><td class=cell>2</td>" +
											"<td class=cell>0</td><td class=cell>0</td></tr><tr class=even><td class=rowhead>M1<A NAME= 'M1'>" +
											"</A></td><td class=cell>1</td><td class=cell>1</td><td class=cell>0</td></tr><tr class=odd>" +
											"<td class=rowhead>M2<A NAME= 'M2'></A></td><td class=cell>1</td><td class=cell>0</td>" +
											"<td class=cell>1</td></tr><tr class=even><td class=rowhead>M3<A NAME= 'M3'></A></td>" +
											"<td class=cell>0</td><td class=cell>2</td><td class=cell>0</td></tr><tr class=odd>" +
											"<td class=rowhead>M4<A NAME= 'M4'></A></td><td class=cell>0</td><td class=cell>1</td>" +
											"<td class=cell>1</td></tr><tr class=even><td class=rowhead>M5<A NAME= 'M5'></A></td>" +
											"<td class=cell>0</td><td class=cell>0</td><td class=cell>2</td></tr></table></td></tr></table>";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gspn = new GSPNNew();
	}
	
	@Override
	protected void tearDown() throws Exception {
		gspn = null;
		super.tearDown();
	}
	
	public void testGspn() throws OutOfMemoryError, TimelessTrapException, ImmediateAbortException, IOException, StateSpaceTooBigException {
		
		DataLayerInterface data = new DataLayer(TestConstants.classicGspn);
		File reachabilityGraph = new File("testresults.rg");
		ResultsHTMLPane dummy = new ResultsHTMLPane(data.getURI());
		
		// Reachability graph generated so that individual GSPN analysis functions can be called
		StateSpaceGenerator.generate(data, reachabilityGraph, dummy);
		
		double[] pi = SteadyStateSolver.solve(reachabilityGraph);
		StateList tangiblestates = new StateList(reachabilityGraph, false);
	
		String states = gspn.renderTangibleStates(data, tangiblestates);
		assertTrue("Tangible states are unexpected", states.equals(expectedTangibleStates));
	}
}
