package starbucks;

import java.io.Serializable;

import static support.Assert.notNull;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class PreparedCoffee implements Serializable {

    private final CoffeeRequestKey requestKey;
    private final long preparationDurationMillis;

    public CoffeeRequestKey getRequestKey() {
        return requestKey;
    }

    public long getPreparationDurationMillis() {
        return preparationDurationMillis;
    }

    public PreparedCoffee(CoffeeRequestKey requestKey, long preparationDurationMillis) {
        this.preparationDurationMillis = preparationDurationMillis;
        notNull(requestKey, "requestKey");

        this.requestKey = requestKey;
    }
}
