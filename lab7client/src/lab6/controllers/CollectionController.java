/*
Интерфейс всех действий, связанных с коллекцией
 */

package lab6.controllers;

import lab6.model.Dragon;

import java.time.LocalDateTime;
import lab6.model.User;

public interface CollectionController extends Iterable<Dragon> {
    LocalDateTime getInitTime();
    int size();
    void add(Dragon dragon);
    boolean containsId(Integer id);
    boolean isEmpty();
    void updateById(Integer id, Dragon dragon);
    void removeById(Integer id);
    void clear();
    void addIfMax(Dragon dragon);
    void removeGreater(Dragon dragon);
    int countByAge(Integer age);
    int countLessThanWingspan(Double wingspan);
    User getOwner(Integer id);
    void updateCollection();
}
