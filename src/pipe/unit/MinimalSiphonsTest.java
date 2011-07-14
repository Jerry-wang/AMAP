package pipe.unit;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import junit.framework.*;
import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.modules.minimalSiphons.*;
import pipe.modules.minimalSiphons.MinimalSiphons.PetriNet;
import pipe.modules.minimalSiphons.MinimalSiphons.SetOfPlaces;

public class MinimalSiphonsTest extends TestCase {
	
	private boolean[] expected;

	public void testModule() {
		
		boolean[] expected = {false, false, false, true, true, true, false, false, true};
		
		MinimalSiphons min = new MinimalSiphons();
		DataLayerInterface pnmlData = new DataLayer(TestConstants.timelessTrap);
		
		List<boolean[]> result = min.getMinimalSiphons(pnmlData);
		
		boolean matches = true;
		
		boolean[] array = result.get(0);
		
		for(int i = 0; i < array.length; i++) {
			
			if (array[i] != expected[i]) matches = false;
			
		}
		
	    assertTrue("Unexpected minimal siphons result obtained", matches);
		
	}

}
