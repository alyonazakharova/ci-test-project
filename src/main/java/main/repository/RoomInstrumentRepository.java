package main.repository;

import main.entity.RoomInstrument;
import org.springframework.data.repository.CrudRepository;

public interface RoomInstrumentRepository extends CrudRepository<RoomInstrument, Long> {
}
