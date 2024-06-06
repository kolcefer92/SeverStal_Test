package ru.zanfirov.severstaldemo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.zanfirov.severstaldemo.entities.Shipment;

import java.time.LocalDate;
import java.util.List;


public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    List<Shipment> findByDateBetween(LocalDate startDate, LocalDate endDate);
}