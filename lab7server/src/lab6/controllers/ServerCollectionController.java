/*
Интерфейс всех действий, связанных с коллекцией
 */

package lab6.controllers;

import lab6.model.Dragon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import lab6.model.User;

public interface ServerCollectionController extends Iterable<Dragon> {
    LocalDateTime getInitTime();
    int size();
    void add(Dragon dragon);
    boolean containsId(Integer id);
    boolean isEmpty();
    void updateById(Integer id, Dragon dragon, User caller);
    void removeById(Integer id, User caller);
    void clear(User sender);
    void addIfMax(Dragon dragon);
    void removeGreater(Dragon dragon);
    int countByAge(Integer age);
    int countLessThanWingspan(Double wingspan);

    ArrayList<Dragon> getCollection();
    int findIndexById(Integer i);
    int getIndexById(Integer i);
    void updateSQLData();
}
