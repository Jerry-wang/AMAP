package pipe.unit;

import pipe.common.dataLayer.Place;
import junit.framework.*;
import static org.mockito.Mockito.*;


public class MockPlace extends TestCase {

	public void testPlace() throws Exception{
		
		Place mockedPlace = mock(Place.class);
		when(mockedPlace.getName()).thenReturn("Name requested");
		
		System.out.println(mockedPlace.getName());
		
	}
}