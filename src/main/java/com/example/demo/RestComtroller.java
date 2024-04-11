package com.example.demo;

import com.example.demo.apiclasses.*;
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
                             @RequestParam String model,
                             @RequestParam String category) {
        try {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setModel(model);
            product.setCategory(category);

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
    public String getProducts(@RequestParam(required = false) String[] filters,@RequestParam(required = false) String category){
        ProductFilter productFilter = new ProductFilter();
        productFilter.setFilters(filters);
        productFilter.setCategory(category);
        return productFilter.getProducts();
    }
    @GetMapping("/cartproducts")
    public String getCartProducts(@RequestParam(required = true) int[] id){
        CartProducts cartProducts = new CartProducts();
        cartProducts.setIds(id);
        return cartProducts.getProducts();
    }
    @PostMapping("/registration")
    public String registration(@RequestParam String login,
                               @RequestParam String password){
        Registration registration = new Registration();
        registration.setLogin(login);
        registration.setPassword(password);
        return registration.regist();
    }
    @GetMapping("/login")
    public String login(@RequestParam(required = true) String login, @RequestParam(required = true) String password){
        Login login1 = new Login();
        login1.setLogin(login);
        login1.setPassword(password);
        return login1.loggin();
    }
    @GetMapping("/cart")
    public String getCart(@RequestParam(required = true)int usersId){
        GetCart getCart = new GetCart();
        getCart.setUserId(usersId);
        return getCart.getItems();
    }
    @PostMapping("/cart")
    public String addCart(@RequestParam int userId,@RequestParam int productId){
        AddCart addCart = new AddCart();
        addCart.setUserId(userId);
        addCart.setProductId(productId);
        return addCart.addToCart();
    }
    @DeleteMapping("/cart")
    public String removeCart(@RequestParam int userId,@RequestParam int productId){
        RemoveCart removeCart = new RemoveCart();
        removeCart.setProductId(productId);
        removeCart.setUserId(userId);
        return removeCart.removeFromCart();
    }
}
