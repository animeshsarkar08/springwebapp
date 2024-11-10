package com.sarkaranimesh.gadgetgalaxy.controllers;
//The controller will allow us to perform CRUD operations

import org.springframework.stereotype.Controller;

//GetMapping, PostMapping, RequestMapping, RequestParam, ModelAttribute
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;// For handling file uploads


import com.sarkaranimesh.gadgetgalaxy.models.*;
import com.sarkaranimesh.gadgetgalaxy.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.InputStream;                          
import java.nio.file.*;
import java.util.Date;                               

import jakarta.validation.Valid;

import java.util.List;


@Controller
@RequestMapping({"","/"})
public class ProductController {
	
	//using repo object we will perform SELECT, INSERT, UPDATE and DELETE
	
	@GetMapping({"","/"})//this is index.html page will open after login
	public String home() {
		return "index"; 
	}
	
		@Autowired
		private ProductRepository repo;
		
		//Show web page with all products 
		@GetMapping("/products")//this method is accessible using url=localhost:8080 or localhost:8080/ 
		public String showProductList (Model model) {
			//sort by direction shows list by descending order of date
			//newer products will be displayed first
			
			//repo perform SELECT operation
			/* SELECT * FROM products
			 * ORDER BY manufactured_date DESC; */
			List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC,"manufacturedDate"));
			//model attribute creates an Object(here its a list) i.e. "products" 
			//which can be accessible through the html file to read
			//Java Program ----> model ------> HTML,so to display anything from java file
			//addAttribute(html object name, java file object name)
			//java.products---->model---->html.products
			model.addAttribute("products",products);
			return "showProducts";//in resources/templates the products folder is defined
		}
		
		//Display Create new product page 
				@GetMapping("/create") //this method is accessible using url=localhost:8080/create
				public String showCreatePage (Model model) {
					
					//ProductDto is need when we make some changes(Insert or Edit)
					//Thus Validation is done using ProductDto Object
					ProductDto productDto = new ProductDto();
					model.addAttribute("productDto",productDto);
					return "CreateProduct";//in resources/templates the products folder is defined
				}
		
		//@Valid will check all the validation of the
				//input into the create new product form and 
				//incase of any validation error the result will be passed to the "result" object of BindingResult class
				//This method perform the INSERT operation
				
				//The productDto contains the submitted data from the form
				@PostMapping("/create") 
				public String createProduct  (@Valid @ModelAttribute ProductDto productDto, BindingResult result){

					//manually validating the imgfile
					/*if(productDto.getImgName().isEmpty()) {
						result.addError(new FieldError("productDto","imgNamed","The image file is required"));
					}*/
					//if there is any validation error than it will be assigned into result object
					if(result.hasErrors()) {
						//if there is any error we will redirect to the same page
						return "CreateProduct";
					}
					
					//saving image file into the server
					//MultipartFile image = productDto.getImgName();
					Date manufacturedDate = new Date();
					//by using timestamp new image name will be generated
					/*String storageFileName = manufacturedDate.getTime() + "-" + image.getOriginalFilename();
					
					try {
						String uploadDir = "public/images/";
						Path uploadPath = Paths.get(uploadDir);
						
						if(!Files.exists(uploadPath)) {
							Files.createDirectories(uploadPath);
						}
						//saving the img file into the server
						try (InputStream inputStream = image.getInputStream()){
							Files.copy(inputStream,Paths.get(uploadDir + storageFileName), 
									StandardCopyOption.REPLACE_EXISTING);
						}
					}catch (Exception e) {
						System.out.println("Exception catched:" + e.getMessage());
					}*/
					
					//here the productDto is the object we receive from the form
					//now we will create a Product object to save the details and 
					//insert into the database
					Product product = new Product();
					product.setName(productDto.getName());
					product.setPrice(productDto.getPrice());
					product.setBrand(productDto.getBrand());
					product.setModelNumber(productDto.getModelNumber());
					product.setColor(productDto.getColor());
					product.setWarranty(productDto.getWarranty());
					product.setBatteryLife(productDto.getBatteryLife());
					product.setRating(productDto.getRating());
					product.setImgName(productDto.getImgName());
					product.setManufacturedDate(manufacturedDate); // Assuming you want to set the current date
					
					//INSERT INTO products (name,...,manufactured_date) VALUES (...);
					repo.save(product);
					//returns the url
					return "redirect:/products";//in resources/templates the products folder is defined
				}
				//Show Details of a Product
				@GetMapping("/show")
				public String showDetails (Model model, @RequestParam int id) {
					try {
						//SELECT * FROM products WHERE product.id = url.id;
						//findById return Optional Type object so need to convert into Product using get() method
						Product product = repo.findById(id).get();
						//creates and html object to read from product object
						// we need ID field which is not present in ProductDto class
						model.addAttribute("product",product);
						
					}catch (Exception e) {
						System.out.println("Exception catched:" + e.getMessage());
						return "redirect:/products";
					}
					return "showDetails";

				}
				
				//Display the edit product page
				@GetMapping("/edit")//products/edit
				public String showEditPage (Model model,
						@RequestParam int id)//RequestParm allow us to read parameter form the url
				{
					//Read the details from the database and send it to the html page
					try {
						//SELECT * FROM products WHERE product.id = url.id;
						//findById return Optional Type object so need to convert into Product using get() method
						Product product = repo.findById(id).get();
						//creates and html object to read from product object
						// we need ID field which is not present in ProductDto class
						model.addAttribute("product",product);
						
						//ProductDto class will check all the validation
						ProductDto productDto = new ProductDto();
						productDto.setName(product.getName());
						productDto.setPrice(product.getPrice());
						productDto.setBrand(product.getBrand());
						productDto.setModelNumber(product.getModelNumber());
						productDto.setColor(product.getColor());
						productDto.setWarranty(product.getWarranty());
						productDto.setBatteryLife(product.getBatteryLife());
						productDto.setRating(product.getRating());
						productDto.setImgName(product.getImgName());
						
						model.addAttribute("productDto",productDto);

						
					}catch (Exception e) {
						System.out.println("Exception catched:" + e.getMessage());
						return "redirect:/products";
					}
					return "EditProduct";
				}
				
				//Write updated data into the database
				@PostMapping("/edit") 
				public String editProduct  (Model model, @RequestParam int id, 
						@Valid @ModelAttribute ProductDto productDto, BindingResult result){

					try {
						Product product = repo.findById(id).get();
						model.addAttribute("product",product);
						
						if(result.hasErrors()) return "EditProduct";
						
						//checking any new image file is received or not
					    /*if(!productDto.getImgName().isEmpty())  //Note here we checking if input is NOT NULL then update the new image
						{
							//deleting the old image
							String uploadDir = "public/images/";
							Path oldPath = Paths.get(uploadDir + product.getImgName()); */
							
							/*
							try {
								Files.delete(oldPath);
							}catch(Exception e) {
								System.out.println("Exception catched:" + e.getMessage());
							}*/
							
							//saving new image file into the server
						/*	MultipartFile image = productDto.getImgName();
							Date manufacturedDate = new Date();
							//by using timestamp new image name will be generated
							String storageFileName = manufacturedDate.getTime() + "-" + image.getOriginalFilename();
								
							try (InputStream inputStream = image.getInputStream()){
									Files.copy(inputStream,Paths.get(uploadDir + storageFileName), 
											StandardCopyOption.REPLACE_EXISTING);
							}catch (Exception e) {
								System.out.println("Exception catched:" + e.getMessage());
							}
							
							//assigning the new image file name into the product object 
							product.setImgName(storageFileName);
						}*/
						
						//updating product object fields from the submitted data 
						//that will be inserted into the database
						product.setName(productDto.getName());
						product.setPrice(productDto.getPrice());
						product.setBrand(productDto.getBrand());
						product.setModelNumber(productDto.getModelNumber());
						product.setColor(productDto.getColor());
						product.setWarranty(productDto.getWarranty());
						product.setBatteryLife(productDto.getBatteryLife());
						product.setRating(productDto.getRating());
						product.setImgName(productDto.getImgName());
						
						//UPDATE products SET name = ..., price= ... WHERE product.id = request.id;
						repo.save(product);
					}catch (Exception e) {
						System.out.println("Exception catched:" + e.getMessage());
						return "EditProduct";
					}
					//returns the url
					return "redirect:/products";//redirect to url "/products"
				}
				
				@GetMapping("/delete")
				public String deleteProduct (@RequestParam int id) {
					
					try {
						Product product = repo.findById(id).get();
						/*
						//delete the image from public/images
						Path imagePath = Paths.get("public/images/" + product.getImgName());
						try {
							Files.delete(imagePath);
						}catch(Exception e) {
							System.out.println("Exception:" + e.getMessage());
						}
						*/
						
						//DELETE FROM products WHERE product.id = html.id;
						repo.delete(product);
						
					}catch(Exception e) {
						System.out.println("Exception:" + e.getMessage());
					}
					return "redirect:/products";
					
				}

}
