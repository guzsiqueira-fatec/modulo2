package br.gov.sp.fatec;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductControllerV1 {
    @GetMapping
    public String getProducts() {
        return "PUBLIC: List of products from API v1";
    }

    @GetMapping("/orders")
    public String getProductOrders(@RequestHeader("userName") String userName) {
        return "PROTECTED: Hey " + userName + ", here are the list of product orders from API v1";
    }
}
