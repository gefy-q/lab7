/*
Для заполнения сокровищ
 */
package lab6.model;

import java.io.Serializable;

public class DragonCave implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L-2l;
    private final long numberOfTreasures;

    public DragonCave(long numberOfTreasures) {
        if (numberOfTreasures <= 0) {
            throw new IllegalArgumentException("Number of treasures must be greater than 0");
        }

        this.numberOfTreasures = numberOfTreasures;
    }

    public long getNumberOfTreasures() {
        return numberOfTreasures;
    }
}
