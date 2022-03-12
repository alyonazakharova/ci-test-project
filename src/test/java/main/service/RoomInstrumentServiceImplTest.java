package main.service;

import main.entity.Instrument;
import main.entity.Room;
import main.entity.RoomInstrument;
import main.repository.RoomInstrumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RoomInstrumentServiceImpl.class})
class RoomInstrumentServiceImplTest {

    @MockBean
    private RoomInstrumentRepository roomInstrumentRepository;

    @Autowired
    private RoomInstrumentService roomInstrumentService;

    @Test
    void shouldAddInstrumentToRoom() {
        RoomInstrument roomInstrument = new RoomInstrument();
        roomInstrumentService.add(roomInstrument);
        verify(roomInstrumentRepository, times(1)).save(roomInstrument);
    }

    @Test
    void shouldDeleteInstrumentFromRoom() {
        RoomInstrument roomInstrument = new RoomInstrument();
        roomInstrument.setId(1L);
        when(roomInstrumentRepository.findById(roomInstrument.getId()))
                .thenReturn(Optional.of(roomInstrument));
        roomInstrumentService.delete(roomInstrument.getId());
        verify(roomInstrumentRepository, times(1)).delete(roomInstrument);
    }

    @Test
    void shouldReturnInstrumentsFromRoom() {
        Room room = new Room();
        room.setId(1L);
        Instrument expectedInstrument = new Instrument();
        RoomInstrument roomInstrument = new RoomInstrument();
        roomInstrument.setRoom(room);
        roomInstrument.setInstrument(expectedInstrument);
        List<RoomInstrument> roomInstrumentList = Collections.singletonList(roomInstrument);

        when(roomInstrumentRepository.findAll()).thenReturn(roomInstrumentList);

        List<Instrument> roomInstruments = roomInstrumentService.getInstrumentsByRoom(1L);
        assertEquals(roomInstruments, Collections.singletonList(expectedInstrument));
        verify(roomInstrumentRepository, times(1)).findAll();
    }

}