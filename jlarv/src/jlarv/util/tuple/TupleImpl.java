package jlarv.util.tuple;

import java.util.Arrays;

/*
 * Credits: https://github.com/ripper234
 * 			http://stackoverflow.com/questions/3642452/java-n-tuple-implementation
 */

/*
  Usage:
    1.- Create new TupleType with the structure of the tuple.
    	final TupleType tripletTupleType =
            TupleType.DefaultFactory.create(
                    Number.class,
                    String.class,
                    Character.class);
                    
    2.- Create a new Tuple:
    	final Tuple t1 = tripletTupleType.createTuple(1, "one", 'a');
 */
public class TupleImpl implements Tuple {

    private final TupleTypeImpl type;
    private final Object[] values;

    TupleImpl(TupleTypeImpl type, Object[] values) {
        this.type = type;
        if (values == null || values.length == 0) {
            this.values = new Object[0];
        } else {
            this.values = new Object[values.length];
            System.arraycopy(values, 0, this.values, 0, values.length);
        }
    }

    @Override
    public TupleTypeImpl getType() {
        return type;
    }

    @Override
    public int size() {
        return values.length;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getNthValue(int i) {
        return (T) values[i];
    }

    @Override
    public boolean equals(Object object) {
        if (object == null)   return false;
        if (this == object)   return true;

        if (! (object instanceof TupleImpl))   return false;

        final TupleImpl other = (TupleImpl) object;
        if (other.size() != size())   return false;

        final int size = size();
        for (int i = 0; i < size; i++) {
            final Object thisNthValue = getNthValue(i);
            final Object otherNthValue = other.getNthValue(i);
            if ((thisNthValue == null && otherNthValue != null) ||
                    (thisNthValue != null && ! thisNthValue.equals(otherNthValue))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        for (Object value : values) {
            if (value != null) {
                hash = hash * 37 + value.hashCode();
            }
        }
        return hash;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}
