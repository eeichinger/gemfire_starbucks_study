package starbucks;

import support.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static support.Assert.notNull;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class CoffeeRequest implements Serializable {

    private final CoffeeRequestKey requestKey;
    private final CoffeeType coffeeType;
    private final long timestampMillis;
    private final List<String> handledByBaristaList = new ArrayList<String>();

    public CoffeeRequestKey getRequestKey() {
        return requestKey;
    }

    public CoffeeType getCoffeeType() {
        return coffeeType;
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public String[] getBaristas() {
        return handledByBaristaList.toArray(new String[0]);
    }
    
    public void markHandledBy(String baristaId) {
        handledByBaristaList.add(baristaId);
    }

    @Override
    public String toString() {
        return "CoffeeRequest{" +
                requestKey +
                ", coffeeType=" + coffeeType +
                ", timestampMillis=" + timestampMillis +
                ", baristas=" + StringUtils.join(handledByBaristaList,",") +
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
