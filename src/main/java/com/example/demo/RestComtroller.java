package com.example.demo;

import com.example.demo.apiclasses.Product;
import com.example.demo.apiclasses.ProductFilter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class RestComtroller {
    @PostMapping("/katalog")
    public String addProduct(@RequestParam String name,
                             @RequestParam int price,
                             @RequestParam MultipartFile image,
                             @RequestParam MultipartFile imagePreview,
                             @RequestParam String model) {
        try {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setModel(model);

            // Получение байтов изображений
            byte[] imageBytes = image.getBytes();
            byte[] imagePreviewBytes = imagePreview.getBytes();

            // Установка байтов изображений в объект Product
            product.setImage(imageBytes);
            product.setImagePreview(imagePreviewBytes);
            return product.addProduct();
        }
        catch (Exception e ){
            return e.toString();
        }
    }
    @GetMapping("/katalog")
    public String getProducts(@RequestParam(required = false) String[] filters){
        ProductFilter productFilter = new ProductFilter();
        productFilter.setFilters(filters);
        return productFilter.getProducts();
    }
}
