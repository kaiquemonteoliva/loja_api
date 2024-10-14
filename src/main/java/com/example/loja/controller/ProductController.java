package com.example.loja.controller;

import com.example.loja.dto.ProductRecord;
import com.example.loja.models.ProductModels;
import com.example.loja.repository.ProductRepository;
import com.example.loja.service.FileUploadService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    FileUploadService fileUploadService;


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> saveProduct(@ModelAttribute @Valid ProductRecord productRecord) {
        var productModel = new ProductModels();
        BeanUtils.copyProperties(productRecord, productModel);
        String urlImg;
        try {
            urlImg = fileUploadService.fazerUpload(productRecord.url_img());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        productModel.setUrl_img(urlImg);

        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }


    @GetMapping
    public ResponseEntity<List<ProductModels>> listarProduct() {
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModels> getone(@PathVariable("id") UUID id) {
        Optional<ProductModels> getProduto = productRepository.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(getProduto.get());
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> updateProducts(@PathVariable("id") UUID id, @ModelAttribute @Valid ProductRecord productRecord) {
        Optional<ProductModels> updateProduto = productRepository.findById(id);
        if (updateProduto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        } else {
            var productModel = updateProduto.get();
            BeanUtils.copyProperties(productRecord, productModel, "url_img");
            if (productRecord.url_img() != null && !productRecord.url_img().isEmpty()) {
                 fileUploadService.deleteOldImage(productModel.getUrl_img());

                try {
                    String newImageUrl = fileUploadService.fazerUpload(productRecord.url_img());
                    productModel.setUrl_img(newImageUrl);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletProducts(@PathVariable("id") UUID id) {
        Optional<ProductModels> productsdel = productRepository.findById(id);
        if (productsdel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        } else {
            productRepository.delete(productsdel.get());
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        productRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}


