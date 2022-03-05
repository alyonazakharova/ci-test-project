package main.service;

import main.entity.Instrument;
import main.exception.EntityNotFoundException;
import main.repository.InstrumentRepository;
import main.repository.RoomInstrumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
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
 * TOTAL: 9
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {InstrumentServiceImpl.class})
class InstrumentServiceImplTest {

    @MockBean
    private InstrumentRepository instrumentRepository;

    @MockBean
    private RoomInstrumentRepository roomInstrumentRepository;

    @Autowired
    private InstrumentService instrumentService;

    @Test
    void shouldAddInstrument() {
        Instrument instrument = new Instrument();
        boolean isInstrumentAdded = instrumentService.add(instrument);
        assertTrue(isInstrumentAdded);
        verify(instrumentRepository, times(1)).save(instrument);
    }

    @Test
    void shouldNotAddInstrumentIfAlreadyExists() {
        Instrument instrument = new Instrument();
        instrument.setId(1L);
        when(instrumentRepository.findById(instrument.getId()))
                .thenReturn(Optional.of(instrument));
        boolean isInstrumentAdded = instrumentService.add(instrument);
        assertFalse(isInstrumentAdded);
        verify(instrumentRepository, times(0)).save(instrument);
    }

    @Test
    void shouldDeleteInstrumentIfFound() {
        Instrument instrument = new Instrument();
        instrument.setId(1L);
        when(instrumentRepository.findById(instrument.getId()))
                .thenReturn(Optional.of(instrument));
        instrumentService.delete(instrument.getId());
        verify(instrumentRepository, times(1)).delete(instrument);
    }

    @Test
    void shouldThrowExceptionIfInstrumentNotFound() {
        Instrument instrument = new Instrument();
        instrument.setId(666L);
        given(instrumentRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> instrumentService.delete(instrument.getId()));
        verify(instrumentRepository, times(0)).delete(instrument);
    }

    @Test
    void shouldReturnInstrumentIfFound() {
        Instrument instrument = new Instrument();
        instrument.setId(1L);
        when(instrumentRepository.findById(instrument.getId()))
                .thenReturn(Optional.of(instrument));
        Instrument expected = instrumentService.getById(instrument.getId());
        assertEquals(expected, instrument);
        verify(instrumentRepository, times(1)).findById(instrument.getId());
    }

    @Test
    void shouldThrowExceptionIfNotFound() {
        Instrument instrument = new Instrument();
        instrument.setId(1L);
        given(instrumentRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> instrumentService.getById(instrument.getId()));
        verify(instrumentRepository, times(1)).findById(instrument.getId());
    }

    @Test
    void shouldFindInstrument() {
        Instrument instrument = new Instrument();
        instrument.setId(1L);
        when(instrumentRepository.findById(instrument.getId()))
                .thenReturn(Optional.of(instrument));
        boolean isFound = instrumentService.checkById(instrument.getId());
        assertTrue(isFound);
        verify(instrumentRepository, times(1)).findById(instrument.getId());
    }

    @Test
    void shouldNotFindInstrumentIfAbsent() {
        Instrument instrument = new Instrument();
        instrument.setId(1L);
        given(instrumentRepository.findById(anyLong())).willReturn(Optional.empty());
        boolean isFound = instrumentService.checkById(instrument.getId());
        assertFalse(isFound);
        verify(instrumentRepository, times(1)).findById(instrument.getId());
    }

    @Test
    void shouldReturnAllInstruments() {
        List<Instrument> instruments = new ArrayList();
        instruments.add(new Instrument());
        given(instrumentRepository.findAll()).willReturn(instruments);
        List<Instrument> expected = instrumentService.getAll();
        assertEquals(expected, instruments);
        verify(instrumentRepository, times(1)).findAll();
    }
}
