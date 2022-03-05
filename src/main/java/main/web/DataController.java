package main.web;

import main.entity.Instrument;
import main.entity.Room;
import main.entity.RoomInstrument;
import main.exception.EntityNotFoundException;
import main.model.InstrumentModel;
import main.model.RoomInstrumentModel;
import main.model.RoomModel;
import org.hibernate.persister.walking.spi.WalkingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import main.service.InstrumentService;
import main.service.RoomInstrumentService;
import main.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {
    private InstrumentService instrumentService;

    private RoomService roomService;

    private RoomInstrumentService roomInstrumentService;

    @PostMapping(value = "/instrument", consumes = "application/json", produces = "application/json")
    public void addInstrument(@RequestBody InstrumentModel instrument) {
        try {
            instrumentService.add(new Instrument(instrument.getName(), instrument.getDescription()));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Instrument already exist");
        }
    }

    @PostMapping(value = "/room", consumes = "application/json", produces = "application/json")
    public void addRoom(@RequestBody RoomModel room) {
        try {
            roomService.add(new Room(room.getName(), room.getDescription(), room.getPrice()));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Instrument already exist");
        }
    }

    @PostMapping(value = "/room_instrument", consumes = "application/json", produces = "application/json")
    public void addRoomInstrument(@RequestBody RoomInstrumentModel roomInstrument) {
        try {
            Room room = roomService.getById(roomInstrument.getRoomId());
            Instrument instrument = instrumentService.getById(roomInstrument.getInstrumentId());
            roomInstrumentService.add(new RoomInstrument(room, instrument));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Instrument in room already exist");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/room/{id}")
    public void deleteRoom(@PathVariable("id") long id) {
        try {
            roomService.delete(id);
        } catch (WalkingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }
    }

    @DeleteMapping("/instrument/{id}")
    public void deleteInstrument(@PathVariable("id") long id) {
        try {
            instrumentService.delete(id);
        } catch (WalkingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Instrument not found");
        }
    }

    @DeleteMapping("/room_instrument/{id}")
    public void deleteRoomInstrument(@PathVariable("id") long id) {
        try {
            roomInstrumentService.delete(id);
        } catch (WalkingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RoomInstrument not found");
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<Instrument>> getAllInstrument() {
        List<Instrument> instrument = instrumentService.getAll();
        return new ResponseEntity<>(instrument, HttpStatus.OK);
    }


    @Autowired
    public void setInstrumentService(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
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
