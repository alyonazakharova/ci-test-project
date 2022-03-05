package main.web;

import main.entity.Customer;
import main.entity.Reservation;
import main.entity.Room;
import main.exception.EntityNotFoundException;
import main.model.ReservationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import main.service.CustomerService;
import main.service.ReservationService;
import main.service.RoomService;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationService reservationService;

    private CustomerService customerService;

    private RoomService roomService;

    @PostMapping(value = "/add", consumes = "application/json")
    public void addReservation(@RequestBody ReservationModel reservation) {
        try {
            Customer customer = customerService.getById(reservation.getCustomerId());
            Room room = roomService.getById(reservation.getRoomId());
            Date date = Date.valueOf(reservation.getDate());
            reservationService.add(new Reservation(date, room, customer));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable("id") long id) {
        try {
            reservationService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void changeConfirmationStatus(@PathVariable("id") long id) {
        try {
            Reservation reservation = reservationService.getById(id);
            boolean currentStatus = reservation.isConfirmed();
            reservation.setConfirmed(!currentStatus);
            reservationService.add(reservation);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/customer/{id}")
    public void deleteCustomerReservations(@PathVariable("id") long id) {
        try {
            reservationService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<Reservation>> getCustomerReservations(@PathVariable("id") long id) {
        List<Reservation> reservations = reservationService.getAllByCustomer(id);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getReservations() {
        List<Reservation> reservations = reservationService.getAll();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @Autowired
    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }
}
