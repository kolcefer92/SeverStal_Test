package ru.zanfirov.severstaldemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zanfirov.severstaldemo.entities.ProductPrice;

import java.time.LocalDate;
import java.util.List;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    List<ProductPrice> findBySupplierAndProductTypeAndDate(String supplier, String productType, LocalDate date);
}