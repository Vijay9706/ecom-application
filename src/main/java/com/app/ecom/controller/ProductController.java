package com.app.ecom.controller;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService productService;
  @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest product) {
      return new ResponseEntity<ProductResponse>(productService.createProduct(product),HttpStatus.CREATED);
    }

  @GetMapping
  public ResponseEntity <List<ProductResponse>> getProducts() {
  return ResponseEntity.ok(productService.getAllProducts());
  };

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }


  @PutMapping("/{id}")
  public ResponseEntity<ProductResponse> UpdateProduct(@PathVariable Long id,@RequestBody ProductRequest updatedProduct) {
    try {
      ProductResponse response = productService.updateProduct(id, updatedProduct);
      return ResponseEntity.ok(response);
    } catch (RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
    boolean deleted=  productService.deleteProduct(id);
      return deleted ?ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
    }
  }



