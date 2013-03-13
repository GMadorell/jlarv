package jlarv.util.tuple;

/*
 * Interface for a tuple of n elements.
 */
public interface Tuple {
	public TupleTypeImpl getType();
	public int size();
	public <T> T getNthValue(int i);
}
