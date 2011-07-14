package pipe.common.dataLayer;

import java.io.Serializable;


/**
 * @author Alex Charalambous (June 2010): Created this
 * class to add support for marking an arbitrary number
 * of token classes. Apart from the actual marking,
 * the type of token to be marked is defined.
 */

public class Marking implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TokenClass tokenClass;
	private int currentMarking;
	
	public Marking(TokenClass tokenClass){
		this.tokenClass = tokenClass;
		currentMarking = 0;
	}
	
	public Marking(TokenClass tokenClass, int marking){
		this.tokenClass = tokenClass;
		currentMarking = marking;
	}

	public TokenClass getTokenClass() {
		return tokenClass;
	}

	public void setTokenClass(TokenClass tokenClass) {
		this.tokenClass = tokenClass;
	}

	public void setCurrentMarking(int marking) {
		currentMarking = marking;
	}
	public int getCurrentMarking() {
		return currentMarking;
	}
}
