package com.example.productrestws;

import com.example.productrestws.controller.ProductController;
import com.example.productrestws.model.Product;
import com.example.productrestws.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

   @MockBean
   private ProductRepository productRepository;
   
   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Test
   void createProduct() throws Exception {      
      Product newProduct = new Product(1, "ABCDE12345", "Laptop Toshiba", 2500, "Rabljeni laptop Toshiba", true);

      mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
         .content(objectMapper.writeValueAsString(newProduct)))
         .andExpect(status().isCreated())
         .andDo(print());
   }

   @Test
   void getProduct() throws Exception {
     long id = 1L;
     Product newProduct = new Product(1, "ABCDE12345", "Laptop Toshiba", 2500, "Rabljeni laptop Toshiba", true);
 
     when(productRepository.findById(id)).thenReturn(Optional.of(newProduct));
     mockMvc.perform(get("/api/products/{id}", id)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.code").value(newProduct.getCode()))
        .andExpect(jsonPath("$.name").value(newProduct.getName()))
        .andExpect(jsonPath("$.priceHrk").value(newProduct.getPriceHrk()))
        .andExpect(jsonPath("$.priceEur").value(newProduct.getPriceEur()))
        .andExpect(jsonPath("$.description").value(newProduct.getDescription()))
        .andExpect(jsonPath("$.isAvailable").value(newProduct.getIsAvailable()))
        .andDo(print());
   }

   @Test
   void getProductNotFound() throws Exception {
     long id = 1L;
 
     when(productRepository.findById(id)).thenReturn(Optional.empty());
     mockMvc.perform(get("/api/products/{id}", id))
         .andExpect(status().isNotFound())
         .andDo(print());
   }

   @Test
   void getAllProducts() throws Exception {
      List<Product> products = new ArrayList<>(
         Arrays.asList(
            new Product(1, "ABCDE12345", "Laptop Toshiba", 2500, "Rabljeni laptop Toshiba", true),
            new Product(2, "CODE654321", "Laptop Lenovo", 3200, "Rabljeni laptop Lenovo", true),
            new Product(3, "CODE112233", "Mobitel Samsung", 4500, "Novi mobitel Samsung", false)
         )
      );
 
      when(productRepository.findAll()).thenReturn(products);
      mockMvc.perform(get("/api/products"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.size()").value(products.size()))
         .andDo(print());
   }

   @Test
   void getProductsByIsAvailable() throws Exception {
      List<Product> products = new ArrayList<>(
         Arrays.asList(
            new Product(1, "ABCDE12345", "Laptop Toshiba", 2500, "Rabljeni laptop Toshiba", true),
            new Product(2, "CODE654321", "Laptop Lenovo", 3200, "Rabljeni laptop Lenovo", true),
            new Product(3, "CODE112233", "Mobitel Samsung", 4500, "Novi mobitel Samsung", true)
         )
      );      
 
      when(productRepository.findByIsAvailable(true)).thenReturn(products);
      mockMvc.perform(get("/api/products/isAvailable"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.size()").value(products.size()))
         .andDo(print());
   }

   @Test
   void updateProduct() throws Exception {
      long id = 1L;

      Product product = new Product(1, "ABCDE12345", "Laptop Toshiba", 2500, "Rabljeni laptop Toshiba", true);
      Product updatedProduct = new Product(1, "ABCDE12345", "Laptop Toshiba Updated", 2000, "Rabljeni laptop Toshiba updated", true);
  
      when(productRepository.findById(id)).thenReturn(Optional.of(product));
      when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
  
      mockMvc.perform(put("/api/products/{id}", id).contentType(MediaType.APPLICATION_JSON)
         .content(objectMapper.writeValueAsString(updatedProduct)))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.code").value(updatedProduct.getCode()))
         .andExpect(jsonPath("$.name").value(updatedProduct.getName()))
         .andExpect(jsonPath("$.priceHrk").value(updatedProduct.getPriceHrk()))
         .andExpect(jsonPath("$.priceEur").value(updatedProduct.getPriceEur()))
         .andExpect(jsonPath("$.description").value(updatedProduct.getDescription()))
         .andExpect(jsonPath("$.isAvailable").value(updatedProduct.getIsAvailable()))
         .andDo(print());
   }

   void updateProductNotFound() throws Exception {
      long id = 1L;

      Product updatedProduct = new Product(1, "ABCDE12345", "Laptop Toshiba Updated", 2000, "Rabljeni laptop Toshiba updated", true);
   
      when(productRepository.findById(id)).thenReturn(Optional.empty());
      when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
  
      mockMvc.perform(put("/api/products/{id}", id).contentType(MediaType.APPLICATION_JSON)
         .content(objectMapper.writeValueAsString(updatedProduct)))
         .andExpect(status().isNotFound())
         .andDo(print());   
   }

   @Test
   void deleteProduct() throws Exception {
     long id = 1L;
 
     doNothing().when(productRepository).deleteById(id);
     mockMvc.perform(delete("/api/products/{id}", id))
        .andExpect(status().isNoContent())
        .andDo(print());
   }
   
   @Test
   void deleteAllProducts() throws Exception {
      doNothing().when(productRepository).deleteAll();
      mockMvc.perform(delete("/api/products"))
         .andExpect(status().isNoContent())
         .andDo(print());
   }
}
