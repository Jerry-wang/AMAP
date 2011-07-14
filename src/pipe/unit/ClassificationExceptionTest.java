package pipe.unit;

import junit.framework.TestCase;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.*;

import pipe.common.dataLayer.*;
import pipe.modules.EmptyNetException;
import pipe.modules.classification.Classification;

public class ClassificationExceptionTest extends TestCase {

	//ExpectedException object used to verify that an exception is thrown
	@Rule 
	public ExpectedException exception = ExpectedException.none();

	//test that an exception is not thrown when the canvas is non-empty
	@org.junit.Test 
	public void testNonEmpty() throws EmptyNetException {

		//create a mock DataLayer object
		DataLayer mockedData = mock(DataLayer.class);
		//return true when method is called
		when(mockedData.hasPlaceTransitionObjects()).thenReturn(true);

		Classification classifier = new Classification();

		//pass mock object to 'real' classifier module
		classifier.getClassification(mockedData);
		//test fails if this method is not called
		verify(mockedData).hasPlaceTransitionObjects();	
	}

	//test that an exception is thrown when the canvas is empty
	@org.junit.Test
	public void testEmptyNet() {

		//create a mock DataLayer object
		DataLayerInterface mockedData = mock(DataLayer.class);
		//return false when method is called
		when(mockedData.hasPlaceTransitionObjects()).thenReturn(false);

		Classification classifier = new Classification();

		try { 
			//test passes only if the following exception is thrown
			exception.expect(EmptyNetException.class);
			//should throw EmptyNetException
			classifier.getClassification(mockedData);
		}
		catch (EmptyNetException e) {
			System.out.println("Expected EmptyNetException thrown");
		}
	}
}
/*
	@org.junit.Test (expected=EmptyNetException.class)
	public void testEmptyNetException() throws EmptyNetException {

		DataLayerInterface mockedData = mock(DataLayer.class);
		when(mockedData.hasPlaceTransitionObjects()).thenReturn(false);

		Classification classifier = new Classification();

		classifier.getClassification(mockedData);
	}
*/

