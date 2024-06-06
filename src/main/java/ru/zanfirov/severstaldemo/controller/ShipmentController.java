package ru.zanfirov.severstaldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.zanfirov.severstaldemo.entities.Product;
import ru.zanfirov.severstaldemo.entities.ProductPrice;
import ru.zanfirov.severstaldemo.entities.Shipment;
import ru.zanfirov.severstaldemo.repositories.ProductPriceRepository;
import ru.zanfirov.severstaldemo.repositories.ShipmentRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ShipmentController {
    private final ShipmentRepository shipmentRepository;
    private final ProductPriceRepository productPriceRepository;

    @Autowired
    public ShipmentController(ShipmentRepository shipmentRepository, ProductPriceRepository productPriceRepository) {
        this.shipmentRepository = shipmentRepository;
        this.productPriceRepository = productPriceRepository;
    }

    @CrossOrigin(origins = "http://127.0.0.1:8080")
    @PostMapping("/shipments")
    public Shipment createShipment(@RequestBody Shipment shipment) {
        for (Product product : shipment.getProducts()) {
            LocalDate currentDate = LocalDate.now();
            Month month = currentDate.getMonth();
            Optional<ProductPrice> productPriceOptional = productPriceRepository.findBySupplierAndProductTypeAndDate(
                            shipment.getSupplier(), product.getProductType(), currentDate)
                    .stream().findFirst();

            if (productPriceOptional.isPresent()) {
                ProductPrice productPrice = productPriceOptional.get();
                product.setPrice(productPrice.getPrice());
            } else {
                product.setPrice(0.0);
            }
        }
        shipment.setDate(LocalDate.now());
        return shipmentRepository.save(shipment);
    }

    @CrossOrigin(origins = "http://127.0.0.1:8080")
    @GetMapping("/reports")
    public List<Shipment> getReports(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return shipmentRepository.findByDateBetween(start, end);
    }


}
