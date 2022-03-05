package main.web;

import main.entity.Instrument;
import main.entity.Room;
import main.entity.RoomInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.service.RoomInstrumentService;
import main.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;

    private RoomInstrumentService roomInstrumentService;

    @GetMapping("/available/{date}")
    public ResponseEntity<List<Room>> getAvailableRooms(@PathVariable("date") String date) {
        List<Room> rooms = roomService.getAvailable(java.sql.Date.valueOf(date));
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/reserved/{date}")
    public ResponseEntity<List<Room>> getReservedRooms(@PathVariable("date") String date) {
        List<Room> rooms = roomService.getReserved(java.sql.Date.valueOf(date));
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAll();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{id}/instruments")
    public ResponseEntity<List<Instrument>> getRoomInstruments(@PathVariable("id") long id) {
        List<Instrument> instruments = roomInstrumentService.getInstrumentsByRoom(id);
        return new ResponseEntity<>(instruments, HttpStatus.OK);
    }

    @GetMapping("/instruments")
    public ResponseEntity<List<RoomInstrument>> getAllRoomsInstruments() {
        List<RoomInstrument> roomInstruments = roomInstrumentService.getAllRoomsInstruments();
        return new ResponseEntity<>(roomInstruments, HttpStatus.OK);
    }

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Autowired
    public void setRoomInstrumentService(RoomInstrumentService roomInstrumentService) {
        this.roomInstrumentService = roomInstrumentService;
    }
}
