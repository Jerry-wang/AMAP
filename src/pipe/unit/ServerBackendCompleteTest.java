package pipe.unit;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;

import pipe.common.AnalysisType;
import pipe.common.PerformanceMeasure;
import pipe.common.SimplePlaces;
import pipe.common.SimpleTransitions;
import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.Unfolder;
import pipe.gui.widgets.ResultsHTMLPane;
import pipe.modules.clientCommon.HTMLPane;
import pipe.modules.clientCommon.SocketIO;
import pipe.modules.steadyState.Analyse;
import pipe.modules.steadyState.FileBrowserPanel;
import pipe.modules.steadyState.StatusListener;
import pipe.modules.steadyState.SteadyState;
import junit.framework.*;

public class ServerBackendCompleteTest extends TestCase {
	
	private String serverAddr = "camelot01.doc.ic.ac.uk";
	private int serverPort = 55500;

	// JList for place names
	JList placesList, transitionList;
	JCheckBox meanStateCBx, varianceStateCBx, stddevStateCBx, distrStateCBx;


	public void testConnection() throws UnknownHostException, IOException {

		DataLayerInterface pnmlData = new DataLayer(TestConstants.simpleColour);
		Unfolder unfolder = new Unfolder(pnmlData);
		pnmlData = unfolder.unfold();
		
		HTMLPane progressPane = mock(HTMLPane.class);

		SimplePlaces splaces = new SimplePlaces(pnmlData);
		SimpleTransitions sTransitions = new SimpleTransitions(pnmlData);
		
		SteadyState ss = new SteadyState(pnmlData);
		FileBrowserPanel fbp = new FileBrowserPanel("Source net", pnmlData);
		ss.setFilePanel(fbp);
		
		JPanel p1 = ss.getStateMeasurePanel();
		    
		JPanel p2 = ss.getCountMeasurePanel();
		
		PerformanceMeasure pm = ss.getSelectedEstimators();
		
		SocketIO serverSock = new SocketIO(serverAddr, serverPort); 
		serverSock.send(AnalysisType.STEADYSTATE);

		serverSock.send(splaces);
		serverSock.send(sTransitions);
		serverSock.send(pm);
		
		String status = new String();

		StatusListener serverListener = new StatusListener(serverSock, progressPane, status);
		status = serverListener.listen();
		System.out.println(status);

		assertTrue("Job did not complete.", status.contains("successfully"));
		
	}
}
