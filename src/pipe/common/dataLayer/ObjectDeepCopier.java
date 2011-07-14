package pipe.common.dataLayer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;


/**
 * @author Alex Charalambous (June 2010): It is common
 * for complex objects such as Markings and TokenClasses
 * to be cloned. As the default clone procedure only does
 * a shadow copy this class creates a deep copy of any
 * object passed in (given that the object is serializable)
 * Especially useful for undo functions.
 */

public class ObjectDeepCopier {
	public static Object deepCopy(Object obj) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream;
		Object deepCopy = null;
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

			objectOutputStream.writeObject(obj);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					byteArrayOutputStream.toByteArray());
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);
			deepCopy = objectInputStream.readObject();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return deepCopy;

	}
	/* Creates a deep copy of the actual marking but retains a direct 
	 * link with the token classes from the model
	 * 
	 */
	public static LinkedList<Marking> mediumCopy(LinkedList<Marking> markings) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream;
		LinkedList<Marking> mediumCopy = null;
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(markings);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					byteArrayOutputStream.toByteArray());
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);
			mediumCopy = (LinkedList<Marking>)objectInputStream.readObject();
			// This defines the medium copy. Replace token classes with 
			// the actual reference from the model so that any updates to the model
			// are reflected in this marking.
			for(int i = 0; i < mediumCopy.size(); i++){
				mediumCopy.get(i).setTokenClass(markings.get(i).getTokenClass());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return mediumCopy;

	}
}
