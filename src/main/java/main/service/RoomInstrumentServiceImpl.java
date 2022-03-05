package main.service;

import main.entity.Instrument;
import main.entity.RoomInstrument;
import main.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import main.repository.RoomInstrumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomInstrumentServiceImpl implements RoomInstrumentService {
    @Autowired
    private RoomInstrumentRepository roomInstrumentRepository;

    @Override
    public void add(RoomInstrument customer) {
        roomInstrumentRepository.save(customer);
    }

    @Override
    public void delete(long id) {
        Optional<RoomInstrument> roomInstrument = roomInstrumentRepository.findById(id);
        if (!roomInstrument.isPresent()) {
            throw new EntityNotFoundException("RoomInstrument not found");
        }
        roomInstrumentRepository.delete(roomInstrument.get());
    }

    @Override
    public List<Instrument> getInstrumentsByRoom(long id) {
        return ((List<RoomInstrument>) roomInstrumentRepository.findAll())
                .stream()
                .filter(roomInstrument -> roomInstrument.getRoom().getId().compareTo(id)==0)
                .map(RoomInstrument::getInstrument)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomInstrument> getAllRoomsInstruments() {
        return (List<RoomInstrument>) roomInstrumentRepository.findAll();
    }
}
