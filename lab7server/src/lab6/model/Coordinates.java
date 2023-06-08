/*
Для работы с координатами
 */
package lab6.model;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private final Long x;
    private final double y;

    public Coordinates(Long x, double y) {
        if (x == null) {
            throw new NullPointerException("X coordinate cannot be null");
        }

        this.x = x;
        this.y = y;
    }

    public Long getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
