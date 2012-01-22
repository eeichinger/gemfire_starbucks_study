package starbucks;

import java.io.Serializable;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class CoffeeRequestKey implements Serializable {
    private final long val;

    public long getVal() {
        return val;
    }

    private CoffeeRequestKey(long val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoffeeRequestKey that = (CoffeeRequestKey) o;

        if (val != that.val) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (val ^ (val >>> 32));
    }

    @Override
    public String toString() {
        return String.format("CoffeeRequestKey[%s]",val);
    }

    public static CoffeeRequestKey newKey() {
        return new CoffeeRequestKey(System.currentTimeMillis());
    }

//    @Override
//    public void writeExternal(ObjectOutput out) throws IOException {
//        out.writeLong(val);
//    }
//
//    @Override
//    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//        val = in.readLong();
//    }
}
