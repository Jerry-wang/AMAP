package pipe.modules.tagged;

import java.io.IOException;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pipe.common.AnalysisSettings;
import pipe.common.AnalysisType;
import pipe.common.SimplePlaces;
import pipe.common.SimpleTransitions;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.modules.clientCommon.SocketIO;

/**
 * This class performs the transfers the pnmlData to the server
 * while updating the user of job progress
 * 
 * @author Barry Kearns
 * @date September 2007
*/


public class Analyse implements Runnable
{
	private String serverAddr = "";
	private int serverPort = 0;
	private ArrayList sourceStateGroups, destStateGroups;
	private AnalysisSettings analysisSetting;
	
	private DataLayerInterface pnmlData;
	JTabbedPane tabbedPane;
	private ProgressBarHTMLPane progressPane;
	private JPanel resultsPanel;
	String status="<h2>Passage Time Analysis Progress</h2>";
	
	
	
	public Analyse(DataLayerInterface pnmlData, ProgressBarHTMLPane progressPane, JPanel resultsPanel)
	{
		this.pnmlData = pnmlData;		 
		this.progressPane = progressPane;
		this.resultsPanel = resultsPanel;
	}
	
	public void setServer(String serverAddr, int serverPort)
	{
	  this.serverAddr = serverAddr;
	  this.serverPort = serverPort;
	}
	
	public void setPassageParameters(ArrayList sourceStateGroups, ArrayList destStateGroups, AnalysisSettings analysisSetting)
	{
		this.sourceStateGroups = sourceStateGroups;
		this.destStateGroups = destStateGroups;
		this.analysisSetting = analysisSetting;
	}
	
	
	public void run()
	{
		// Convert the PNML data into a serialisable form for transmission
		SimplePlaces splaces = new SimplePlaces(pnmlData);
		SimpleTransitions sTransitions = new SimpleTransitions(pnmlData);
		
		
		  try
		  {
			  updateUI("Opening Connection");
			  SocketIO serverSock = new SocketIO(serverAddr, serverPort); 
			  serverSock.send(AnalysisType.PASSAGETIME); // Inform server of the process to be performed
			  
			  updateUI("Sending data");
			  serverSock.send(splaces);
			  serverSock.send(sTransitions);
			  serverSock.send(sourceStateGroups);
			  serverSock.send(destStateGroups);
			  serverSock.send(analysisSetting);
			  
			  updateUI("Server Scheduling Process");
			  StatusListener serverListener = new StatusListener(serverSock, progressPane, status);
			  status = serverListener.listen();
			  
			
			  
			  updateUI("Receiving Results");
			  ResultsReceiver resultsReceiver = new ResultsReceiver(serverSock, resultsPanel, status);
			  resultsReceiver.receive();
			  
			  updateUI("Closing Connection");		  
			  serverSock.close();
			  
			  
			  // Slow the transition between progress tab and results tab
			  try {
				Thread.sleep(800);
			  } catch (InterruptedException e) {} 

			  
			  			  
			  // Add results pane and set active
			  tabbedPane.addTab("Results", resultsPanel);
			  tabbedPane.setSelectedComponent(resultsPanel);
			  
		  }
		  catch (StreamCorruptedException sce) {
		    	 updateUI("Stream Corrupted Exception" + sce.getMessage());
		  }
		  catch(UnknownHostException uhe){
		    	 updateUI("Unknown host exception " + uhe.getMessage());
		  }
		  catch (OptionalDataException ode) {
		   	 updateUI("Data Exception" + ode.getMessage());
		  }
		  catch (IOException ioe) {
		   	 updateUI("Unable to connect to server " + serverAddr + " : " +  serverPort + ": " + ioe.getMessage());	 
	      }
		
	}
	
	private void updateUI(String update)
	{
	  // setText is a thread safe operation so we can freely use it within this thread
	  status += update + "<br>";
	  progressPane.setText(status); 
	}


	public void setTabbedPane(JTabbedPane inputPane)
	{
		tabbedPane = inputPane;
	}

}
