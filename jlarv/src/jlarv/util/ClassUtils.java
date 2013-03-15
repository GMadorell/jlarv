package jlarv.util;

/**
 * Class that provides static methods for utility around class names and classes in general.
 * @author Gerard Madorell Sala
 */
public class ClassUtils {
	/**
	 * Example of usage: getClassName(java.awt.Frame.class) -> returns Frame
	 * @param c - The class we want to get the name from.
	 * @return The name of the class.
	 */
	public static String getClassName(@SuppressWarnings("rawtypes") Class c) {
		String FQClassName = c.getName();
		int firstChar;
		firstChar = FQClassName.lastIndexOf ('.') + 1;
		if ( firstChar > 0 ) {
			FQClassName = FQClassName.substring ( firstChar );
		}
		return FQClassName;
    }
}
