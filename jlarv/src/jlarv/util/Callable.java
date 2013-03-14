package jlarv.util;

import java.util.ArrayList;

/*
 * Interface used whenever we want to pass a function to a World instance.
 * Must implement the function we want to execute in a call method.
 * Usage: Search for Anonymous Classes.   
 */
public interface Callable {
	public Object call(ArrayList<Object> args); // Maybe static??
}
