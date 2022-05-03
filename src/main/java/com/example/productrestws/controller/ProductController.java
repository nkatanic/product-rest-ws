package com.example.productrestws.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.validation.FieldError;

import com.example.productrestws.model.Product;
import com.example.productrestws.repository.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@Validated
public class ProductController {
    
    @Autowired
    ProductRepository productRepository;

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/products")
	public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
		try {
			Product newProduct = new Product(product.getCode(), product.getName(), product.getPriceHrk(), product.getDescription(), product.getIsAvailable());
			
			// Calculate price in EUR
			float priceEur = this.calculatePriceEur(newProduct.getPriceHrk());
			
			newProduct.setPriceEur(priceEur);
			Product responseProduct = productRepository.save(newProduct);

			return new ResponseEntity<>(responseProduct, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
		try {
			List<Product> products = new ArrayList<Product>();		
			productRepository.findAll().forEach(products::add);
		
			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(products, HttpStatus.OK);            
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") @Min(1) long id) {
		Optional<Product> productData = productRepository.findById(id);
		if (productData.isPresent()) {
			return new ResponseEntity<>(productData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/products/isAvailable")
	public ResponseEntity<List<Product>> findByIsAvailable() {
		try {
			List<Product> products = productRepository.findByIsAvailable(true);
			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") @Min(1) long id, @Valid @RequestBody Product product) {
		Optional<Product> productData = productRepository.findById(id);
		if (productData.isPresent()) {
			
			Product updatedProduct = productData.get();
			updatedProduct.setCode(product.getCode());
			updatedProduct.setName(product.getName());
			updatedProduct.setPriceHrk(product.getPriceHrk());

			float priceEur = this.calculatePriceEur(updatedProduct.getPriceHrk());
			updatedProduct.setPriceEur(priceEur);

			updatedProduct.setDescription(product.getDescription());
			updatedProduct.setIsAvailable(product.getIsAvailable());
	
			return new ResponseEntity<>(productRepository.save(updatedProduct), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") @Min(1) long id) {
		try {
			productRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/products")
	public ResponseEntity<HttpStatus> deleteAllProducts() {
		try {
			productRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private float calculatePriceEur(float priceHrk) {
		
		RestTemplate restTemplate = new RestTemplate();
		String hnbApiUrl = "https://api.hnb.hr/tecajn/v1?valuta=EUR";		
		HnbCurrency[] hnbCurrency = restTemplate.getForObject(hnbApiUrl, HnbCurrency[].class);
		String conversionRateString = hnbCurrency[0].getConversionRate();
		conversionRateString = conversionRateString.replace(',', '.');
		float conversionRate = Float.parseFloat(conversionRateString);
		float priceEur = priceHrk / conversionRate;

		return priceEur;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    	Map<String, String> errors = new HashMap<>();
    	ex.getBindingResult().getAllErrors().forEach((error) -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
    });
    	return errors;
	}
}
