package pipe.unit;

import junit.framework.TestCase;
import pipe.common.dataLayer.DataLayer;
import pipe.common.dataLayer.DataLayerInterface;
import pipe.modules.EmptyNetException;
import pipe.modules.classification.*;

public class ClassificationTest extends TestCase {

	private Classification classifier;
	
	private boolean[] expected = {true,true,true,true,true,true};
	
	@Override
	protected void setUp() throws Exception {
		classifier = new Classification();
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		classifier = null;
		super.tearDown();
	}
	
	public void testClassifier() throws EmptyNetException {
		
		DataLayerInterface data = new DataLayer(TestConstants.classicGspn);
		boolean result[] = classifier.getClassification(data);
		boolean matches = true;
		
		for (int i = 0; i < result.length; i++){
			if(result[i]!=expected[i]) matches = false;
		}
		
		assertTrue("Expected classification not found", matches);
	}
	
}
