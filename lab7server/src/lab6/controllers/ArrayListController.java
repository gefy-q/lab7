/*
Действия, связанные с коллекцией
 */

package lab6.controllers;

import java.sql.*;
import lab6.model.Dragon;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lab6.Main;
import lab6.model.Coordinates;
import lab6.model.DragonCave;
import lab6.model.DragonCharacter;
import lab6.model.User;

public class ArrayListController implements ServerCollectionController {
    private final ArrayList<Dragon> dragons = new ArrayList<>();
    private final LocalDateTime initTime = LocalDateTime.now();
    public ArrayListController() {
        updateSQLData();
    }
    @Override
    public final void updateSQLData() {
        ResultSet data = Main.sqlController.getRaw("SELECT dragons.*, users.username FROM dragons JOIN users ON dragons.owner = users.id ORDER BY name ASC;");
        if(data == null)
            throw new IllegalArgumentException("Не удалось получить данные о коллекции");
        dragons.clear();
        try {
            while(data.next()) {
                Dragon dragon = new Dragon(
                        data.getInt("id"),
                        data.getString("name"),
                        new Coordinates(
                                data.getLong("coordinates_x"),
                                data.getDouble("coordinates_y")
                        ),
                        new java.util.Date(data.getDate("creation_date").getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        data.getInt("age"),
                        data.getString("description"),
                        data.getDouble("wingspan"),
                        DragonCharacter.values()[data.getInt("dragon_character")],
                        new DragonCave(data.getLong("cave_treasures")),
                        new User(
                                data.getInt("owner"),
                                data.getString("username")
                        )
                );
                dragons.add(dragon);
            }
        } catch(SQLException ex) {
            System.err.println("Не удалось обновить данные коллекции");
        }
    }
    @Override
    public ArrayList<Dragon> getCollection() {
        // updateSQLData();
        return (ArrayList<Dragon>)dragons.stream().sorted(Comparator.comparing(Dragon::getName)).collect(Collectors.toList());
    }
    
    @Override
    public int findIndexById(Integer id) {
        for (int i = 0; i < dragons.size(); ++i) {
            if (dragons.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getIndexById(Integer id) {
        int index = findIndexById(id);
        if (id == -1) {
            throw new NoSuchElementException();
        }
        return index;
    }

    @Override
    public LocalDateTime getInitTime() {
        return this.initTime;
    }

    @Override
    public int size() {
        return dragons.size();
    }

    @Override
    public void add(Dragon dragon) {
        try {
            dragon.setServer(-1, LocalDateTime.now());
            
            PreparedStatement statement = Main.sqlController.getConnection().prepareStatement("INSERT INTO dragons (" +
                "name, coordinates_x, coordinates_y, creation_date, age, description, wingspan, dragon_character, cave_treasures, owner) VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, dragon.getName());
            statement.setLong(2, dragon.getCoordinates().getX());
            statement.setDouble(3, dragon.getCoordinates().getY());
            statement.setDate(4, java.sql.Date.valueOf(dragon.getCreationDate().toLocalDate()));
            statement.setInt(5, dragon.getAge());
            statement.setString(6, dragon.getDescription());
            statement.setDouble(7, dragon.getWingspan());
            statement.setInt(8, dragon.getCharacter().ordinal());
            statement.setLong(9, dragon.getCave().getNumberOfTreasures());
            statement.setInt(10, dragon.getOwner().getId());
            
            if(Main.sqlController.send(statement))
                dragons.add(dragon);
        } catch (SQLException ex) {
            System.err.println("Не удалось добавить дракона");
        }
    }

    @Override
    public boolean containsId(Integer id) {
        return findIndexById(id) != -1;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void updateById(Integer id, Dragon dragon, User caller) {
        try {
            if(dragons.get(getIndexById(id)).getOwner().getId() != caller.getId())
                return;
            dragon.setServer(id, LocalDateTime.now());
            
            PreparedStatement statement = Main.sqlController.getConnection().prepareStatement("UPDATE dragons SET " +
                "name = ?, coordinates_x = ?, coordinates_y = ?, creation_date = ?, age = ?, description = ?, wingspan = ?, dragon_character = ?, cave_treasures = ? WHERE\n" +
                "id = ?;");
            statement.setString(1, dragon.getName());
            statement.setLong(2, dragon.getCoordinates().getX());
            statement.setDouble(3, dragon.getCoordinates().getY());
            statement.setDate(4, java.sql.Date.valueOf(dragon.getCreationDate().toLocalDate()));
            statement.setInt(5, dragon.getAge());
            statement.setString(6, dragon.getDescription());
            statement.setDouble(7, dragon.getWingspan());
            statement.setInt(8, dragon.getCharacter().ordinal());
            statement.setLong(9, dragon.getCave().getNumberOfTreasures());
            statement.setInt(10, id);
            
            if(Main.sqlController.send(statement))
                dragons.set(getIndexById(id), dragon);
        } catch (SQLException ex) {
            System.err.println("Не удалось обновить дракона");
        } catch (NoSuchElementException ex) {}
    }

    @Override
    public void removeById(Integer id, User caller) {
        if(Main.sqlController.sendRaw("DELETE FROM dragons WHERE id IN ("+id+") AND owner = "+caller.getId()))
            updateSQLData();
    }

    @Override
    public void clear(User sender) {
        if(Main.sqlController.sendRaw("DELETE FROM dragons WHERE id > 0 AND owner = "+sender.getId()))
            dragons.clear();
            updateSQLData();
    }

    @Override
    public void addIfMax(Dragon dragon) {
        for (Dragon value : dragons) {
            if (value.getCave().getNumberOfTreasures() >= dragon.getCave().getNumberOfTreasures()) {
                return;
            }
        }

        add(dragon);
    }

    @Override
    public void removeGreater(Dragon dragon) {
        if(Main.sqlController.sendRaw("DELETE FROM dragons WHERE cave_treasures > "+dragon.getCave().getNumberOfTreasures()+" AND owner = "+dragon.getOwner().getId()))
            updateSQLData();
    }

    @Override
    public int countByAge(Integer age) {
        long count = dragons.stream()
                .filter(x -> x.getAge().equals(age))
                .count();
        return Math.toIntExact(count);
    }

    @Override
    public int countLessThanWingspan(Double wingspan) {
        long count = dragons.stream()
                .filter(x -> x.getWingspan() < wingspan)
                .count();
        return Math.toIntExact(count);
    }

    @Override
    public Iterator<Dragon> iterator() {
        return dragons.iterator();
    }
}
