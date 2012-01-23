package starbucks;

import java.io.Serializable;

import static support.Assert.notNull;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class PreparedCoffee implements Serializable {

    private final String baristaId;
    private final CoffeeRequestKey requestKey;
    private final long preparationDurationMillis;

    public String getBaristaId() {
        return baristaId;
    }

    public CoffeeRequestKey getRequestKey() {
        return requestKey;
    }

    public long getPreparationDurationMillis() {
        return preparationDurationMillis;
    }

    public PreparedCoffee(String baristaId, CoffeeRequestKey requestKey, long preparationDurationMillis) {
        notNull(baristaId, "baristaId");
        notNull(requestKey, "requestKey");

        this.baristaId = baristaId;
        this.preparationDurationMillis = preparationDurationMillis;
        this.requestKey = requestKey;
    }
}
