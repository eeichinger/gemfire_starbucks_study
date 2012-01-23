package starbucks;

import java.io.Serializable;

/**
 * @author: Erich Eichinger
 * @date: 22/01/12
 */
public class BaristaStatistics implements Serializable {
    private int numberOfBaristas;
    private int totalPendingQueueSize;

    public int getNumberOfBaristas() {
        return numberOfBaristas;
    }

    public int getTotalPendingQueueSize() {
        return totalPendingQueueSize;
    }

    @Override
    public String toString() {
        return "BaristaStatistics{" +
                "numberOfBaristas=" + numberOfBaristas +
                ", totalPendingQueueSize=" + totalPendingQueueSize +
                '}';
    }

    public BaristaStatistics(int numberOfBaristas, int totalPendingQueueSize) {
        this.numberOfBaristas = numberOfBaristas;
        this.totalPendingQueueSize = totalPendingQueueSize;
    }

    public BaristaStatistics add(BaristaStatistics other) {
        return new BaristaStatistics(this.numberOfBaristas+other.numberOfBaristas, this.totalPendingQueueSize+other.totalPendingQueueSize);
    }

    public final static BaristaStatistics EMPTY = new BaristaStatistics(0,0);
}
