package jlarv.util.tuple;

/**
 * Represents a type of tuple.  Used to define a type of tuple and then
 * create tuples of that type.
 */
public interface TupleType {

    public int size();

    public Class<?> getNthType(int i);

    /**
     * Tuple are immutable objects.  Tuples should contain only immutable objects or
     * objects that won't be modified while part of a tuple.
     *
     * @param values
     * @return Tuple with the given values
     * @throws IllegalArgumentException if the wrong # of arguments or incompatible tuple values are provided
     */
    public TupleImpl createTuple(Object... values);

    public class DefaultFactory {
        public static TupleType create(final Class<?>... types) {
            return new TupleTypeImpl(types);
        }
    }

}
