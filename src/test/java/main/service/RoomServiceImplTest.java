package main.service;

import main.entity.Room;
import main.exception.EntityNotFoundException;
import main.repository.ReservationRepository;
import main.repository.RoomInstrumentRepository;
import main.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TOTAL: 6
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RoomServiceImpl.class})
class RoomServiceImplTest {

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private RoomInstrumentRepository roomInstrumentRepository;

    @Test
    void shouldAddRoomWithUniqueName() {
        Room room = new Room();
        boolean isRoomAdded = roomService.add(room);
        assertTrue(isRoomAdded);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void shouldNotAddRoomIfNameExists() {
        Room room = new Room();
        room.setName("Test room");
        when(roomRepository.findByName(room.getName()))
                .thenReturn(Optional.of(room));
        boolean isRoomAdded = roomService.add(room);
        assertFalse(isRoomAdded);
        verify(roomRepository, times(0)).save(room);
    }

    @Test
    void shouldDeleteRoomIfFound() {
        Room room = new Room();
        room.setId(1L);
        when(roomRepository.findById(room.getId()))
                .thenReturn(Optional.of(room));
        roomService.delete(room.getId());
        verify(roomRepository, times(1)).delete(room);
    }

    @Test
    void shouldThrowExceptionIfRoomNotFound() {
        Room room = new Room();
        room.setId(1L);
        given(roomRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> roomService.delete(room.getId()));
        verify(roomRepository, times(0)).delete(room);
    }

    @Test
    void shouldReturnRoomIfFound() {
        Room room = new Room();
        room.setId(1L);
        when(roomRepository.findById(room.getId()))
                .thenReturn(Optional.of(room));
        Room expected = roomService.getById(room.getId());
        assertEquals(expected, room);
        verify(roomRepository, times(1)).findById(room.getId());
    }

    @Test
    void shouldThrowExceptionIfNotFound() {
        Room room = new Room();
        room.setId(1L);
        given(roomRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> roomService.getById(room.getId()));
        verify(roomRepository, times(1)).findById(room.getId());
    }
}
