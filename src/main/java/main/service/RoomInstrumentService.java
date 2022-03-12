package main.service;

import main.entity.Instrument;
import main.entity.RoomInstrument;

import java.util.List;

public interface RoomInstrumentService {
    void add(RoomInstrument roomInstrument);

    void delete(long id);

    List<Instrument> getInstrumentsByRoom(long id);

    List<RoomInstrument> getAllRoomsInstruments();
}
