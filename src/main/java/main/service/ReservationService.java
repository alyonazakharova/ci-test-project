package main.service;

import main.entity.Reservation;

import java.util.List;

public interface ReservationService {
    boolean add(Reservation reservation);

    void delete(long id);

    void deleteAllByCustomer(long id);

    List<Reservation> getAllByCustomer(long id);

    List<Reservation> getAll();

    Reservation getById(long id);
}
