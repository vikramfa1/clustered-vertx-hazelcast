package com.example.demo.service;

import com.example.demo.model.Customer;
import org.springframework.stereotype.Service;
import rx.Single;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    List<Customer> customerList = new ArrayList<>();

    @PostConstruct
    public void createInfo() {
        Customer customer1 = new Customer();
        customer1.id = 234;
        customer1.name = "vignesh";
        customerList.add(customer1);

        Customer customer2 = new Customer();
        customer2.id = 235;
        customer2.name = "venkatesh";
        customerList.add(customer2);
    }

    public Single<List<Customer>> getCustomer() {
        return Single.just(customerList);
    }

    public Single<Optional<Customer>> getCustomerIdReactive(int id) {
        return Single.just(customerList.stream().filter(a -> a.id == id).findAny());
    }

    public Optional<Customer> getCustomerId(int id) {
        return customerList.stream().filter(a -> a.id == id).findAny();
    }

    public void addCustomer(Customer customer) {
        customerList.add(customer);
    }
}
