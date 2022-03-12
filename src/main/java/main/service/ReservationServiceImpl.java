package main.service;

import main.entity.Reservation;
import main.exception.EntityNotFoundException;
import main.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public boolean add(Reservation reservation) {
        if (reservation.getDate().toLocalDate().isBefore(LocalDate.now())) {
            return false;
        }
        Optional<Reservation> reservationFromDB = reservationRepository.findById(reservation.getId());
        if (reservationFromDB.isPresent()) {
            return false;
        }
        reservationRepository.save(reservation);
        return true;
    }

    @Override
    public void delete(long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (!reservation.isPresent()) {
            throw new EntityNotFoundException("Reservation not found");
        }
        reservationRepository.delete(reservation.get());
    }

    @Override
    public List<Reservation> getAllByCustomer(long id) {
        return ((List<Reservation>) reservationRepository.findAll())
                .stream()
                .filter(reservation -> reservation.getCustomer().getId().compareTo(id) == 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getAll() {
        return (List<Reservation>) reservationRepository.findAll();
    }

    @Override
    public Reservation getById(long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.orElse(null);
    }
}
