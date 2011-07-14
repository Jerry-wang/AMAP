package pipe.test;

import java.io.RandomAccessFile;
import java.io.File;

import pipe.common.dataLayer.calculations.MarkingState;
import pipe.common.dataLayer.calculations.State;
import pipe.io.StateRecord;

import junit.framework.TestCase;

/** 
 * This JUnit TestCase tests the various read/write functions 
 * in StateRecord
 * @author Edwin Chung Mar 2007
 *
 */

public class StateRecordTest extends TestCase {
	
	private RandomAccessFile raFile;
	private File testFile;
	
	protected void setUp() throws Exception {
		super.setUp();
		testFile = new File ("testFile");
		raFile = new RandomAccessFile(testFile, "rw");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		raFile.close();
		testFile.delete();
		
	}

	public void testReadWrite () throws Exception {
		
		final State testState1 = new State (new int []{1,0,1,0,1,0});
		final State testState2 = new State (new int []{0,1,0,1,0,1}); 
		final MarkingState testMarking1 = new MarkingState(testState1, 0, false);
		final MarkingState testMarking2 = new MarkingState(testState2, 1, true);
		StateRecord record1 = new StateRecord (testMarking1);
		StateRecord record2 = new StateRecord (testMarking2);
		StateRecord result = new StateRecord();
				
		record1.write(raFile);
		record2.write(raFile);
		record1.write(raFile, testMarking1.isTangible);
		record2.write(raFile, testMarking1.isTangible);
		raFile.seek(0);
		result.read(6, raFile);
		State output1 = new State(result.getState());
		result.read(6, raFile);
		State output2 = new State(result.getState());
		result.read1(6, raFile);
		MarkingState output3 = new MarkingState (new State(result.getState()), 2, result.getTangible());
		result.read1(6, raFile);
		MarkingState output4 = new MarkingState (new State(result.getState()), 2, result.getTangible());
		
		assertTrue(testState1.equals(output1));
		assertFalse(testState1.equals(output2));
		assertTrue(testState2.equals(output2));
		assertTrue(testMarking1.equals(output3));
		assertFalse(testMarking1.equals(output4));
		assertTrue(testMarking2.equals(output4));
	}
	
}
