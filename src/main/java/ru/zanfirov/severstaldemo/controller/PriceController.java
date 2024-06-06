package ru.zanfirov.severstaldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.zanfirov.severstaldemo.service.CsvDataImporter;


@RestController
@RequestMapping("/api")
public class PriceController {
    private final CsvDataImporter csvDataImporter;

    @Autowired
    public PriceController(CsvDataImporter csvDataImporter) {
        this.csvDataImporter = csvDataImporter;
    }


    // @CrossOrigin(origins = "http://127.0.0.1:8080")
    @PostMapping("/prices/upload")
    public void uploadCsv(@RequestParam("file") MultipartFile file) {
        csvDataImporter.importPricesFromFile(file);
    }
}
