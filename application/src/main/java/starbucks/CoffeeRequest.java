package starbucks;

import java.io.Serializable;

import static support.Assert.notNull;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class CoffeeRequest implements Serializable {

    private final CoffeeRequestKey requestKey;
    private final CoffeeType coffeeType;
    private final long timestampMillis;

    public CoffeeRequestKey getRequestKey() {
        return requestKey;
    }

    public CoffeeType getCoffeeType() {
        return coffeeType;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    @Override
    public String toString() {
        return "CoffeeRequest{" +
                requestKey +
                ", coffeeType=" + coffeeType +
                ", timestampMillis=" + timestampMillis +
                "}";
    }

    private CoffeeRequest(CoffeeType coffeeType, CoffeeRequestKey requestKey, long timestampMillis) {
        notNull(coffeeType, "coffeeType"); notNull(requestKey, "requestKey"); notNull(timestampMillis, "timestampMillis");

        this.coffeeType = coffeeType;
        this.requestKey = requestKey;
        this.timestampMillis = timestampMillis;
    }

    public static CoffeeRequest newCoffeeRequest(CoffeeType coffeeType) {
        return new CoffeeRequest(coffeeType, CoffeeRequestKey.newKey(), System.currentTimeMillis());
    }
}
