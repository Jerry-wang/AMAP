package pipe.test;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import pipe.common.dataLayer.Arc;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.Marking;
import pipe.common.dataLayer.Place;
import pipe.common.dataLayer.TokenClass;
import pipe.common.dataLayer.Transition;
import pipe.common.dataLayer.Unfolder;
import pipe.gui.CreateGui;
import pipe.gui.GuiFrame;
import junit.framework.TestCase;

public class UnfolderTest extends TestCase {
	private DataLayerInterface newDatalayer;
	public UnfolderTest(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		CreateGui.getApp().dispose();
		int lastIndex = CreateGui.getFreeSpace();
		while(lastIndex >= 0){
			CreateGui.removeTab(0); //deletes static ref to net objects
			lastIndex--;			//kept by CreateGui
		}
	}
	
	public void testUnfolder(){
		File testFile = new File(TestConstants.testFileDualProcessor);
		assertTrue("Test file is not present. All other tests depend on this",
				testFile.exists());
		CreateGui.init();
		GuiFrame frame = CreateGui.getApp();
		// Load up our test file
		frame.createNewTab(testFile, false);
		DataLayerInterface datalayer = CreateGui.getModel();
		Unfolder unfolder = new Unfolder(datalayer);
		newDatalayer = unfolder.unfold();

		int counter = 0;
		for(TokenClass tc:newDatalayer.getTokenClasses()){
			if(tc.isEnabled())
				counter++;
		}

		assertTrue("There should only be one token class enabled but instead there are: " + counter
				, counter ==1);

		assertTrue("There should be 7 places but instead there are: " 
				+ newDatalayer.getPlacesCount(), newDatalayer.getPlacesCount() == 7 
		);
		checkPlaceMarking("P0_Blue", 1);
		checkPlaceMarking("P1_Blue", 0);
		checkPlaceMarking("P2_Blue", 0);
		checkPlaceMarking("Resource_Default", 1);
		checkPlaceMarking("P0_Red", 1);
		checkPlaceMarking("P1_Red", 0);
		checkPlaceMarking("P2_Red", 0);
		
		LinkedList<String> parents = new LinkedList<String>();
		LinkedList<String> children = new LinkedList<String>();
		
		parents.add("P0_Blue");
		children.add("P1_Blue");
		checkTransitionConnections("T0", parents, children);
		
		parents.add("P0_Red");
		children.add("P1_Red");
		checkTransitionConnections("T1", parents, children);
		
		parents.add("P1_Blue");
		parents.add("Resource_Default");
		children.add("P2_Blue");
		checkTransitionConnections("T3", parents, children);
		
		parents.add("P1_Red");
		parents.add("Resource_Default");
		children.add("P2_Red");
		checkTransitionConnections("T2", parents, children);
		
		parents.add("P2_Blue");
		children.add("Resource_Default");
		children.add("P0_Blue");
		checkTransitionConnections("T4", parents, children);
		
		parents.add("P2_Red");
		children.add("Resource_Default");
		children.add("P0_Red");
		checkTransitionConnections("T5", parents, children);
		
		parents.add("P0_Blue");
		children.add("P1_Blue");
		checkTransitionConnections("T0", parents, children);
		
		parents.add("P0_Blue");
		children.add("P1_Blue");
		checkTransitionConnections("T0", parents, children);
		

		
		
	}
	
	public void testReadersWriters(){
		/**
		 * Now do the same with another example. The readers and 
		 * writers coloured net
		 */
		CreateGui.init();
		File testFile = new File(TestConstants.testFileReadersWriters);
		assertTrue("Test file is not present. All other tests depend on this",
				testFile.exists());
		GuiFrame frame = CreateGui.getApp();
		// Load up our test file
		frame.createNewTab(testFile, false);
		DataLayerInterface datalayer = CreateGui.getModel();
		Unfolder unfolder = new Unfolder(datalayer);
		newDatalayer = unfolder.unfold();


		int counter = 0;
		for(TokenClass tc:newDatalayer.getTokenClasses()){
			if(tc.isEnabled())
				counter++;
		}

		assertTrue("There should only be one token class enabled but instead there are: " + counter
				, counter ==1);

		assertTrue("There should be 5 places but instead there are: " 
				+ newDatalayer.getPlacesCount(), newDatalayer.getPlacesCount() == 5 
		);
		checkPlaceMarking("Waiting for access_Readers", 3);
		checkPlaceMarking("Accessing_Readers", 0);
		checkPlaceMarking("Resource_Default", 3);
		checkPlaceMarking("Waiting for access_Writers", 2);
		checkPlaceMarking("Accessing_Writers", 0);
		
		LinkedList<String> parents = new LinkedList<String>();
		LinkedList<String> children = new LinkedList<String>();
		
		parents.add("Waiting for access_Readers");
		parents.add("Resource_Default");
		children.add("Accessing_Readers");
		checkTransitionConnections("T3", parents, children);
		
		parents.add("Accessing_Readers");
		children.add("Waiting for access_Readers");
		children.add("Resource_Default");
		checkTransitionConnections("T5", parents, children);
		
		parents.add("Waiting for access_Writers");
		parents.add("Resource_Default");
		children.add("Accessing_Writers");
		checkTransitionConnections("T4", parents, children);
		
		parents.add("Accessing_Writers");
		children.add("Waiting for access_Writers");
		children.add("Resource_Default");
		checkTransitionConnections("T6", parents, children);
		
		checkArcWeight("Resource_Default", "T6", 3);
		checkArcWeight("T4", "Resource_Default", 3);
		checkArcWeight("Waiting for access_Writers", "T6", 1);
	
	}
	
	private void checkPlaceMarking(String placeName, int expectedMarking){
		LinkedList<Marking> markings = newDatalayer.getPlaceById(placeName).getCurrentMarking();
		boolean noOtherTokenClass = true;
		for(Marking m:markings)
		{
			if(m.getTokenClass().getID().equals("Default")){
				assertTrue("Place " + placeName + " was supposed to have: " + expectedMarking
						+ " tokens but instead has: " + m.getCurrentMarking(), 
						m.getCurrentMarking() == expectedMarking);
			}
			else if(m.getCurrentMarking() != 0){
				noOtherTokenClass = false;
				break;
			}
		}
		assertTrue("Place: "+ placeName + " is marked with a coloured token!"
					, noOtherTokenClass);
	}
	private void checkTransitionConnections(String transition,
			LinkedList<String> parents, LinkedList<String> children) {
		Transition t = newDatalayer.getTransitionByName(transition);
		Arc anArc;
		// Check transition's children
		Iterator<?> it = t.getArcsFrom();
		int childCounter = 0;
		while (it.hasNext()) {
			anArc = (Arc) it.next();
			String targetName = anArc.getTarget().getName();
			assertTrue("Place: " + targetName
					+ " should not be a child of transition: " + transition,
					children.contains(targetName));
			childCounter++;
		}
		assertTrue("Transition: " + transition + " should have "
				+ parents.size() + " parents", childCounter == children.size());

		// Check transition's parents
		it = t.getArcsTo();
		int parentCounter = 0;
		while (it.hasNext()) {
			anArc = (Arc) it.next();
			String sourceName = anArc.getSource().getName();
			assertTrue("Place: " + sourceName
					+ " should not be a parent of transition: " + transition,
					parents.contains(sourceName));
			parentCounter++;
		}
		assertTrue("Transition: " + transition + " should have "
				+ parents.size() + " parents", parentCounter == parents.size());
		parents.clear();
		children.clear();
	}
	
	private void checkArcWeight(String childName, String parentName,
			int expectedArcWeight) {
		Place p;
		Transition t;
		boolean childIsTransition = true;
		t = newDatalayer.getTransitionByName(childName);
		// Hence the child is not a transition and must be
		// a place
		if (t == null){ 
			childIsTransition = false;
		}
		if(childIsTransition){
			p = newDatalayer.getPlaceByName(parentName);
		}
		else{
			p = newDatalayer.getPlaceByName(childName);
			t = newDatalayer.getTransitionByName(parentName);
		}
		Arc anArc;
		// Check transition's children
		Iterator<?> it;
		if (childIsTransition){
			it = t.getArcsTo();
		}
		else{
			it = t.getArcsFrom();
		}
		while (it.hasNext()) {
			anArc = (Arc) it.next();
			String targetName = anArc.getTarget().getName();
			if(targetName.equals(p.getName())){
				for(Marking m:anArc.getWeight()){
					if(m.getTokenClass().getID().equals("Default")){
						assertTrue("Arc with child: " + childName + 
								" and parent: " + parentName + " was supposed" +
								"to have a marking of: " + expectedArcWeight + 
								" but instead has one of: " + m.getCurrentMarking(),
									expectedArcWeight == m.getCurrentMarking());
					}
				}
			}
		}
	}
}
