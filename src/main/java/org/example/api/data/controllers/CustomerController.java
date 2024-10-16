package org.example.api.data.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.api.data.entity.Customer;
import org.example.api.service.AuthService;
import org.example.api.service.CustomerService;
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {
  private final CustomerService customerService;

  @Autowired
  private AuthService authService;

  @Autowired
  private Token token;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping("/api/customer/{id}")   // get 1 customer by customerId
  public Optional<Customer> customer(@PathVariable Integer id) {
    return customerService.findById(id);
  }

  @GetMapping("/api/customers")      // get all customers from DB
  public List<Customer> customer(HttpServletRequest request) {
    return customerService.findAll();
  }

  @DeleteMapping("/public/customer/{email}")
  public ResponseEntity<String> deleteCustomer(@PathVariable String email) {
    try {
      boolean isDeleted = customerService.deleteByEmail(email);
      if (isDeleted) {
        return ResponseEntity.ok("Customer deleted successfully");
      } else {
        return ResponseEntity.badRequest().body("Customer not found");
      }
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error occurred during deletion");
    }
  }
}

