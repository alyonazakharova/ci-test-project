package main.service;

import main.entity.Reservation;
import main.exception.EntityNotFoundException;
import main.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TOTAL: 5
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ReservationServiceImpl.class})
class ReservationServiceImplTest {

    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;

    @Test
    void shouldAddReservation() {
        Reservation reservation = new Reservation();
        reservation.setDate(Date.valueOf(LocalDate.now().plusDays(1)));
        boolean isReservationAdded = reservationService.add(reservation);
        assertTrue(isReservationAdded);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void shouldNotAddReservationIfAlreadyExists() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setDate(Date.valueOf(LocalDate.now().plusDays(1)));
        when(reservationRepository.findById(reservation.getId()))
                .thenReturn(Optional.of(reservation));
        boolean isReservationAdded = reservationService.add(reservation);
        assertFalse(isReservationAdded);
        verify(reservationRepository, times(0)).save(reservation);
    }

    @Test
    void shouldNotCreateReservationForPastDate() {
        Reservation reservation = new Reservation();
        reservation.setDate(Date.valueOf(LocalDate.now().minusDays(1)));
        boolean isReservationCreated = reservationService.add(reservation);
        assertFalse(isReservationCreated);
    }

    @Test
    void shouldDeleteReservationIfFound() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        when(reservationRepository.findById(reservation.getId()))
                .thenReturn(Optional.of(reservation));
        reservationService.delete(reservation.getId());
        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    void shouldThrowExceptionIfReservationNotFound() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        given(reservationRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> reservationService.delete(reservation.getId()));
        verify(reservationRepository, times(0)).delete(reservation);
    }
}
