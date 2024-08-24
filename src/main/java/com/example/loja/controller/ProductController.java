package com.example.loja.controller;

import com.example.loja.dto.ProductRecord;
import com.example.loja.models.ProductModels;
import com.example.loja.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@CrossOrigin
@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    @CrossOrigin
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    Metodo aonde vou receber a requisição
    public ResponseEntity<Object> saveProduct(@ModelAttribute @Valid ProductRecord productRecord){
//        Ativando o model
        var productModel = new ProductModels();
//        Convertendo o DTO que veio pela requisição para Json para o Model
        BeanUtils.copyProperties(productRecord, productModel);
//        Dando o Retorno da requisição
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }
//    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<ProductModels>> listarProduct(){
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModels> getone(@PathVariable("id")UUID id){
        Optional<ProductModels> getProduto = productRepository.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(getProduto.get());
    }
    @CrossOrigin
    @PutMapping("/{id}")
//    O Opitional é um container que lida com valores que podem estar vazios ou não. Evitando o uso execivo de verificação de nulidade.
//    O Object trada diferentes valores retornando ou não variados tipos de valores.
    public ResponseEntity<Object> updateProducts(@PathVariable("id")UUID id, @RequestBody @Valid ProductRecord productRecord){
        Optional<ProductModels> updateProduto = productRepository.findById(id);
        if (updateProduto.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }else{

            var productModels = updateProduto.get();
            BeanUtils.copyProperties(productRecord, productModels);
            return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModels));

        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletProducts(@PathVariable("id")UUID id ){
        Optional<ProductModels> productsdel = productRepository.findById(id);
        if (productsdel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }else {
            productRepository.delete(productsdel.get());
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        // Deleta todos os produtos do repositório
        productRepository.deleteAll();

        // Retorna uma resposta vazia com status 204 No Content
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
