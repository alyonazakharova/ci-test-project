package main.service;

import main.entity.Room;

import java.sql.Date;
import java.util.List;

public interface RoomService {
    boolean add(Room customer);

    void delete(long id);

    Room getById(long id);

    boolean checkById(long id);

    List<Room> getAll();

    List<Room> getAvailable(Date date);

    List<Room> getReserved(Date date);
}
