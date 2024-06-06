package ru.zanfirov.severstaldemo.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import ru.zanfirov.severstaldemo.entities.ProductPrice;
import ru.zanfirov.severstaldemo.repositories.ProductPriceRepository;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;



@Service

public class CsvDataImporter {
    private final ProductPriceRepository productPriceRepository;

    public CsvDataImporter(ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    public void importPricesFromFile(MultipartFile file) {
        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            for (CSVRecord record : records) {
                String supplier = record.get("Supplier");
                String productType = record.get("Product Type");
                double price = Double.parseDouble(record.get("Price"));
                LocalDate date = LocalDate.parse(record.get("Date"));

                ProductPrice productPrice = new ProductPrice();
                productPrice.setSupplier(supplier);
                productPrice.setProductType(productType);
                productPrice.setPrice(price);
                productPrice.setDate(date);

                productPriceRepository.save(productPrice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}