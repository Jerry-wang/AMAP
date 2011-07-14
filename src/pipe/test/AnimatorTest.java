package pipe.test;

import java.io.File;
import java.util.LinkedList;
import junit.framework.TestCase;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.common.dataLayer.GroupTransition;
import pipe.common.dataLayer.Marking;
import pipe.common.dataLayer.Place;
import pipe.common.dataLayer.Transition;
import pipe.gui.Animator;
import pipe.gui.CreateGui;
import pipe.gui.GuiFrame;
import pipe.gui.GuiView;
import pipe.gui.undo.UndoableEdit;

public class AnimatorTest extends TestCase {
	private DataLayerInterface datalayer;

	public AnimatorTest(String name) {
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
  //      @Test
	public void testE(){
		File testFile = new File(TestConstants.testFileDualProcessor);
		assertTrue("Test file is not present. All other tests depend on this",
				testFile.exists());
		CreateGui.init();
		GuiFrame frame = CreateGui.getApp();
		// Load up our test file
		frame.createNewTab(testFile, false);
		GuiView view = CreateGui.getView();
		datalayer = CreateGui.getModel();
		
		assertTrue("PetriNet editor is initially not in animation mode",
				!view.isInAnimationMode());
		
		frame.executeAction("toggleAnimation");
		assertTrue("Once set to animate, PetriNet editor is in animation mode",
				view.isInAnimationMode());
		
		Animator animator = CreateGui.getAnimator();
		LinkedList<String> shouldBeEnabled = new LinkedList<String>();
		LinkedList<String> shouldBeVisible = new LinkedList<String>();
		
		
		shouldBeEnabled.add("T0");
		shouldBeEnabled.add("T1");
		// Only transitions t0 and t1 should be enabled
		checkEnabled(shouldBeEnabled);
		checkPlaceMarking("P0", 0, 1, 1);
		checkPlaceMarking("P1", 0, 0, 0);
		checkPlaceMarking("P2", 0, 0, 0);
		checkPlaceMarking("Resource", 1, 0, 0);
		
		animator.fireTransition(datalayer.getTransitionById("T0"));
		shouldBeEnabled.add("T1");
		shouldBeEnabled.add("T3");
		// Only transitions t1 and t3 should be enabled
		checkEnabled(shouldBeEnabled);
		checkPlaceMarking("P0", 0, 1, 0);
		checkPlaceMarking("P1", 0, 0, 1);
		checkPlaceMarking("P2", 0, 0, 0);
		checkPlaceMarking("Resource", 1, 0, 0);
		
		
		// If we fire a disabled transition nothing happens
		// and we are in the exact same state as before
		animator.fireTransition(datalayer.getTransitionById("T0"));
		shouldBeEnabled.add("T1");
		shouldBeEnabled.add("T3");
		// Only transitions t1 and t3 should be enabled
		checkEnabled(shouldBeEnabled);
		checkPlaceMarking("P0", 0, 1, 0);
		checkPlaceMarking("P1", 0, 0, 1);
		checkPlaceMarking("P2", 0, 0, 0);
		checkPlaceMarking("Resource", 1, 0, 0);
		
		animator.fireTransition(datalayer.getTransitionById("T1"));
		shouldBeEnabled.add("T2");
		shouldBeEnabled.add("T3");
		// Only transitions t2 and t3 should be enabled
		checkEnabled(shouldBeEnabled);
		checkPlaceMarking("P0", 0, 0, 0);
		checkPlaceMarking("P1", 0, 1, 1);
		checkPlaceMarking("P2", 0, 0, 0);
		checkPlaceMarking("Resource", 1, 0, 0);
		
		animator.stepBack();
		shouldBeEnabled.add("T1");
		shouldBeEnabled.add("T3");
		// Only transitions t1 and t3 should be enabled
		checkEnabled(shouldBeEnabled);
		checkPlaceMarking("P0", 0, 1, 0);
		checkPlaceMarking("P1", 0, 0, 1);
		checkPlaceMarking("P2", 0, 0, 0);
		checkPlaceMarking("Resource", 1, 0, 0);
		
		animator.stepForward();
		shouldBeEnabled.add("T2");
		shouldBeEnabled.add("T3");
		// Only transitions t2 and t3 should be enabled
		checkEnabled(shouldBeEnabled);
		checkPlaceMarking("P0", 0, 0, 0);
		checkPlaceMarking("P1", 0, 1, 1);
		checkPlaceMarking("P2", 0, 0, 0);
		checkPlaceMarking("Resource", 1, 0, 0);
		
		animator.stepBack();
		shouldBeEnabled.add("T1");
		shouldBeEnabled.add("T3");
		// Only transitions t1 and t3 should be enabled
		checkEnabled(shouldBeEnabled);
		checkPlaceMarking("P0", 0, 1, 0);
		checkPlaceMarking("P1", 0, 0, 1);
		checkPlaceMarking("P2", 0, 0, 0);
		checkPlaceMarking("Resource", 1, 0, 0);
		
		animator.fireTransition(datalayer.getTransitionById("T3"));
		shouldBeEnabled.add("T4");
		shouldBeEnabled.add("T1");
		// Only transitions t4 and t1 should be enabled
		checkEnabled(shouldBeEnabled);
		checkPlaceMarking("P0", 0, 1, 0);
		checkPlaceMarking("P1", 0, 0, 0);
		checkPlaceMarking("P2", 0, 0, 1);
		checkPlaceMarking("Resource", 0, 0, 0);
		
		animator.fireTransition(datalayer.getTransitionById("T1"));
		shouldBeEnabled.add("T4");
		// Only transition t4 should be enabled
		checkEnabled(shouldBeEnabled);
		checkPlaceMarking("P0", 0, 0, 0);
		checkPlaceMarking("P1", 0, 1, 0);
		checkPlaceMarking("P2", 0, 0, 1);
		checkPlaceMarking("Resource", 0, 0, 0);
		
		animator.fireTransition(datalayer.getTransitionById("T4"));
		shouldBeEnabled.add("T0");
		shouldBeEnabled.add("T2");
		// Only transition t4 should be enabled
		checkEnabled(shouldBeEnabled);
		checkPlaceMarking("P0", 0, 0, 1);
		checkPlaceMarking("P1", 0, 1, 0);
		checkPlaceMarking("P2", 0, 0, 0);
		checkPlaceMarking("Resource", 1, 0, 0);
		
		
		// See assertion below. A red token and a blue token should
		// never acquire the resource (and hence be in p2) at the same time
		
		boolean twoTokensAtOnce = false;
		Place p = datalayer.getPlaceById("P2");
		for(int i = 0; i < 1; i++){
			animator.doRandomFiring();
			int tokenTypeCounter = 0;
			for(Marking m:p.getCurrentMarking()){
				if(m.getCurrentMarking() > 0){
					tokenTypeCounter++;
				}
			}
			if(tokenTypeCounter > 1){
				twoTokensAtOnce = true;
				break;
			}
		}
		assertTrue("Ensure that both a red token and a blue token never" +
				"acquire the resource at the same time during 100 random firings", 
				!twoTokensAtOnce);
		
		frame.executeAction("toggleAnimation");
		assertTrue("PetriNet editor is no longer in animation mode",
				!view.isInAnimationMode());		
		assertTrue("PetriNet editor is no longer in animation mode",
				!view.isInAnimationMode());	
		
		// Since we are no longer in animation mode, transitions should
		// not be enabled and not be highlighted. (hence enabledTransitionNames
		// is passed empty)
		checkEnabled(shouldBeEnabled);
		
		// Initial marking should also have been restored
		checkPlaceMarking("P0", 0, 1, 1);
		checkPlaceMarking("P1", 0, 0, 0);
		checkPlaceMarking("P2", 0, 0, 0);
		checkPlaceMarking("Resource", 1, 0, 0);
		
		// This is to give the GUI the chance to process all necessary
		/*// instructions to exit from animation mode.
		try {
			Thread.sleep(2200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		
		/* Now test Group Transitions
		 * 
		 */
		frame.executeAction("groupTransitionsAction");

		// No transitions should be visible now as they have
		// been replaced by groupTransitions
		checkTransitionVisibility(shouldBeVisible);
		
		shouldBeVisible.add("T0_T1");
		shouldBeVisible.add("T2_T3");
		shouldBeVisible.add("T4_T5");
		checkGroupTransitionCorrectness(shouldBeVisible, shouldBeEnabled, 3);
		
		frame.executeAction("ungroupTransitionsAction");
		// Grouping of transitions has been undone. Hence groupTransitions
		// should no longer be visible
		checkGroupTransitionCorrectness(shouldBeVisible, shouldBeEnabled, 0);
		for(int i = 0; i < 6; i++){
			shouldBeVisible.add("T" + i);
		}
		checkTransitionVisibility(shouldBeVisible);
		

		// Select t0,t1 and t2 on canvas. 
		Transition t0 = datalayer.getTransitionById("T0");
		t0.select();
		Transition t1 = datalayer.getTransitionById("T1");
		t1.select();
		Transition t2 = datalayer.getTransitionById("T2");
		t2.select();
//		Then try to group them.
		t0.groupTransitions();
		// This should not be possible. Here we test to make sure
		// that although attempted this action has never occurred.
		// Note that this test requires user invervention because a 
		// popup alerting the user of this appears and must be OKed.
		checkGroupTransitionCorrectness(shouldBeVisible, shouldBeEnabled, 0);
		for(int i = 0; i < 6; i++){
			shouldBeVisible.add("T" + i);
		}
		

		
		// Select t0 and t1 on canvas. 
		t0.select();
		t1.select();
//		Then try to group them.
		UndoableEdit edit = t0.groupTransitions();
    	CreateGui.getView().getUndoManager().addNewEdit(edit);

		// There should now be 1 visible group transition
		shouldBeVisible.add("T0_T1");
		checkGroupTransitionCorrectness(shouldBeVisible, shouldBeEnabled, 1);
		for(int i = 0; i < 6; i++){
			if(i != 0 && i != 1)
				shouldBeVisible.add("T" + i);
		}
		checkTransitionVisibility(shouldBeVisible);
		
		// Undo grouping action
		view.getUndoManager().doUndo();

		checkGroupTransitionCorrectness(shouldBeVisible, shouldBeEnabled, 0);
		for(int i = 0; i < 6; i++){
			shouldBeVisible.add("T" + i);
		}
		checkTransitionVisibility(shouldBeVisible);
		
		// Redo grouping action
		view.getUndoManager().doRedo();
		// There should now be 1 visible group transition
		shouldBeVisible.add("T0_T1");
		checkGroupTransitionCorrectness(shouldBeVisible, shouldBeEnabled, 1);
		for(int i = 0; i < 6; i++){
			if(i != 0 && i != 1)
				shouldBeVisible.add("T" + i);
		}
		checkTransitionVisibility(shouldBeVisible);
		
		/************* Now let's go back to animation mode
		 * Only this time with GroupTransitions
		 * 
		 */
		frame.executeAction("groupTransitionsAction");
		frame.executeAction("toggleAnimation");
		assertTrue("Once set to animate, PetriNet editor is in animation mode",
				view.isInAnimationMode());
		
		// Initial setup should be like this:
		shouldBeVisible.add("T0_T1");
		shouldBeVisible.add("T2_T3");
		shouldBeVisible.add("T4_T5");
		shouldBeEnabled.add("T0_T1");
		checkGroupTransitionCorrectness(shouldBeVisible, shouldBeEnabled, 3);
		checkTransitionVisibility(shouldBeVisible);
		
		animator.fireTransition(datalayer.getTransitionByName("T0"));
		shouldBeVisible.add("T0_T1");
		shouldBeVisible.add("T2_T3");
		shouldBeVisible.add("T4_T5");
		shouldBeEnabled.add("T0_T1");
		shouldBeEnabled.add("T2_T3");
		checkGroupTransitionCorrectness(shouldBeVisible, shouldBeEnabled, 3);
		checkTransitionVisibility(shouldBeVisible);

		frame.executeAction("toggleAnimation");
		assertTrue("Once out of animate, PetriNet editor is not in animation mode",
				!view.isInAnimationMode());
		shouldBeVisible.add("T0_T1");
		shouldBeVisible.add("T2_T3");
		shouldBeVisible.add("T4_T5");
		checkGroupTransitionCorrectness(shouldBeVisible, shouldBeEnabled, 3);
		checkTransitionVisibility(shouldBeVisible);
		
	}
	
	/*
	 *  Ensures that only inputted transitions are enabled and no others.
	 *  Also ensures that the enabled transitions are highlighted red and
	 *  no other transitions are highlighted red.
	 */
	private void checkEnabled(LinkedList<String> transitionNames){
		Transition[] transitions = datalayer.getTransitions();
		for(Transition transition:transitions){
			if(transitionNames.contains(transition.getName())){
				assertTrue(transition.getName() + " should be enabled at this point"
						,transition.isEnabled());
				assertTrue(transition.getName() + " should be highlighted at this point"
						,transition.highlighted);
			}
			else{
				assertTrue(transition.getName() + " should not be enabled at this point"
						,!transition.isEnabled());
				assertTrue(transition.getName() + " should not be highlighted at this point"
						,!transition.highlighted);
			}
		}
		transitionNames.clear();
	}
	
	/* Checks the inputed place's markings against the expected markings (those inputed)
	 * 
	 */
	private void checkPlaceMarking(String placeName, int defaultMarking, 
			int redMarking, int blueMarking){
		Place p = datalayer.getPlaceById(placeName);
		LinkedList<Marking> marking = p.getCurrentMarking();
		for(Marking m:marking){
			if(m.getTokenClass().getID().equals("Default")){
				assertTrue("There should be " + defaultMarking + " tokens of type Default in place: " 
						+ placeName + " at this instance",
						m.getCurrentMarking() == defaultMarking);
			}
			else if(m.getTokenClass().getID().equals("Blue")){
				assertTrue("There should be " + blueMarking + " tokens of type Blue in place: " 
						+ placeName + " at this instance",
						m.getCurrentMarking() == blueMarking);
			}
			else if(m.getTokenClass().getID().equals("Red")){
				assertTrue("There should be " + redMarking + " token of type Red in place: " 
						+ placeName + " at this instance",
						m.getCurrentMarking() == redMarking);
			}
		}
	}
	
	/* Checks that inputted transitions are not visible if they have been
	 * replaced by grouped transitions.
	 * 
	 */
	private void checkTransitionVisibility(LinkedList<String> shouldBeVisible){
		Transition[] transitions = datalayer.getTransitions();
		for(Transition t:transitions){
			if(!shouldBeVisible.contains(t.getName())){
				assertTrue("Transition " + t.getName() + " should not be visible at this point",
					!t.isVisible());
				assertTrue("The label of transition " + t.getName() + " should not be visible at this point",
					!t.getNameLabel().isVisible());
			}
			else{
				assertTrue("Transition " + t.getName() + " should be visible at this point",
						t.isVisible());
					assertTrue("The label of transition " + t.getName() + " should be visible at this point",
						t.getNameLabel().isVisible());
			}
		}
		shouldBeVisible.clear();
	}
	
	/*
	 *  Ensures that only inputted groupTransitions are enabled and no others.
	 *  Also ensures that the enabled groupTransitions are highlighted red and
	 *  no other groupTransitions are highlighted red. Also checks total
	 *  number of expected group transitions present.
	 */

	private void checkGroupTransitionCorrectness(LinkedList<String> shouldBeVisible, 
			LinkedList<String> shouldBeEnabled, int expectedNumber){
		Transition[] transitions = datalayer.getTransitions();
		LinkedList<GroupTransition> groupedTransitions = new LinkedList<GroupTransition>();
		for(int i = 0; i < transitions.length; i++){
			if(transitions[i].isGrouped()){
				if(!groupedTransitions.contains(transitions[i].getGroup()))
					groupedTransitions.add(transitions[i].getGroup());
			}
		}
		for(GroupTransition groupTransition:groupedTransitions){
				assertTrue("Problem with GroupTransition: " + groupTransition.getName() + 
						". Expected visibility to be: " + shouldBeVisible.contains(groupTransition.getName())
						+ " but in reality is: " + groupTransition.isVisible(),
						groupTransition.isVisible() == 
							shouldBeVisible.contains(groupTransition.getName()));
					assertTrue("Problem with the label of groupTransition " + groupTransition.getName() + 
							". Expected visibility to be: " + shouldBeVisible.contains(groupTransition.getName())
							+ " but in reality is: " + groupTransition.getNameLabel().isVisible(),
						groupTransition.getNameLabel().isVisible() ==
							shouldBeVisible.contains(groupTransition.getName()));

				assertTrue("Problem with GroupTransition: " + groupTransition.getName() + 
						". Expected enabled to be: " + shouldBeEnabled.contains(groupTransition.getName())
						+ " but in reality is: " + groupTransition.isEnabled(),
						groupTransition.isEnabled() == 
							shouldBeEnabled.contains(groupTransition.getName()));
					assertTrue("Problem with GroupTransition: " + groupTransition.getName() + 
							". Expected highlighted to be: " + shouldBeEnabled.contains(groupTransition.getName())
							+ " but in reality is: " + groupTransition.highlighted,
						groupTransition.highlighted == 
							shouldBeEnabled.contains(groupTransition.getName()));
		}
		assertTrue("There should be " + expectedNumber + " grouped transitions in total, " +
				"where in fact there are: " + groupedTransitions.size(), 
				groupedTransitions.size() == expectedNumber);
		shouldBeVisible.clear();
		shouldBeEnabled.clear();
	}
	
	
	
}
