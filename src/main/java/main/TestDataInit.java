package main;

import main.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import main.repository.*;

import java.sql.Date;

@Component
public class TestDataInit implements CommandLineRunner {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    InstrumentRepository instrumentRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    RoomInstrumentRepository roomInstrumentRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder pwdEncoder;

    @Override
    public void run(String[] args) {
        Room room = new Room("Room", "description", 100L);
        Room room1 = new Room("Room 1", "description", 1100L);
        Room room2 = new Room("Room 2", "description", 900L);
        Room room3 = new Room("Room 3", "description", 200L);
        Room room4 = new Room("Room 4", "description", 1400L);

        roomRepository.save(room);
        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);
        roomRepository.save(room4);

        Instrument instrument = new Instrument("Fender guitar", "description");
        Instrument instrument1 = new Instrument("Gibson guitar", "description");
        Instrument instrument2 = new Instrument("Pearl drum kit", "description");
        Instrument instrument3 = new Instrument("Peavey guitar amp", "description");
        Instrument instrument4 = new Instrument("Tama snare", "description");

        instrumentRepository.save(instrument);
        instrumentRepository.save(instrument1);
        instrumentRepository.save(instrument2);
        instrumentRepository.save(instrument3);
        instrumentRepository.save(instrument4);

        roomInstrumentRepository.save(new RoomInstrument(room, instrument));

        User user = new User("admin", pwdEncoder.encode("password"), "ADMIN");
        userRepository.save(user);

        Customer customer = new Customer("name", "+79643423523", user);
        customerRepository.save(customer);

        Reservation reservation = new Reservation(Date.valueOf("2021-12-18"), room, customer);
        Reservation reservation1 = new Reservation(Date.valueOf("2022-01-11"), room1, customer);
        Reservation reservation2 = new Reservation(Date.valueOf("2022-01-12"), room2, customer);
        reservationRepository.save(reservation);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
    }
}
