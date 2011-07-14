package pipe.test;

public final class TestConstants {

		  public static final String testFileDualProcessor = "src/pipe/test/resources/Dual Processor With Colour.xml";
		  public static final String testFileReadersWriters = "src/pipe/test/resources/Coloured Reader Writer.xml";
		  public static final String simpleTestMultipleWeights = "src/pipe/test/resources/Simple Coloured Net.xml";
		  public static final String classicGspn = "src/pipe/test/resources/ClassicGSPN.xml";
		  /**
		  * The caller references the constants using <tt>Consts.EMPTY_STRING</tt>, 
		  * and so on. Thus, the caller should be prevented from constructing objects of 
		  * this class, by declaring this private constructor. 
		  */
		  private TestConstants(){
		    throw new AssertionError();
		  }
		
}
