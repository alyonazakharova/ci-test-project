package main.service;

import main.entity.Instrument;

import java.util.List;

public interface InstrumentService {
    boolean add(Instrument customer);

    void delete(long id);

    Instrument getById(long id);

    boolean checkById(long id);

    List<Instrument> getAll();
}
