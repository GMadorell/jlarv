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
		String class_name = c.getName();
		int first_char;
		first_char = class_name.lastIndexOf ('.') + 1;
		if (first_char > 0 ) {
			class_name = class_name.substring(first_char);
		}
		return class_name;
    }
}
