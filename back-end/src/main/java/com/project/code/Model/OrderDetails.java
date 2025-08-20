package com.project.code.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class OrderDetails {

    // 1. Add 'id' field:
    //    - Type: private Long
    //    - This field will be auto-incremented.
    //    - Use @Id to mark it as the primary key.
    //    - Use @GeneratedValue(strategy = GenerationType.IDENTITY) to auto-increment it.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. Add 'customer' field:
    //    - Type: private Customer
    //    - This field refers to the customer who placed the order.
    //    - Use @ManyToOne with @JoinColumn(name = "customer_id") to define the foreign key relationship.
    //    - Apply @JsonManagedReference to handle bidirectional relationships and JSON serialization.
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonManagedReference
    private Customer customer;

    // 3. Add 'store' field:
    //    - Type: private Store
    //    - This field refers to the store from where the order was placed.
    //    - Use @ManyToOne with @JoinColumn(name = "store_id") to define the foreign key relationship.
    //    - Apply @JsonManagedReference to handle bidirectional relationships and JSON serialization.
    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonManagedReference
    private Store store;

    // 4. Add 'totalPrice' field:
    //    - Type: private Double
    //    - This field represents the total price of the order.
    private Double totalPrice;

    // 5. Add 'date' field:
    //    - Type: private LocalDateTime
    //    - This field represents the date and time when the order was placed.
    private LocalDate date;

    // 6. Add 'orderItems' field:
    //    - Type: private List<OrderItem>
    //    - This field represents the list of items in the order.
    //    - Use @OneToMany(mappedBy = "order", fetch = FetchType.EAGER) to establish the one-to-many relationship with OrderItem.
    //    - Apply @JsonManagedReference to prevent circular references during JSON serialization.
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    // 7. Add constructors:
    //    - A no-argument constructor.
    //    - A parameterized constructor that accepts Customer, Store, totalPrice, and date as parameters.

    public OrderDetails() {
    }

    public OrderDetails(Customer customer, Store store, Double totalPrice, LocalDate date) {
        this.customer = customer;
        this.store = store;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    // 8. Add @Entity annotation:
    //    - Use @Entity above the class name to mark it as a JPA entity.

    // 9. Add Getters and Setters:
    //    - Add getter and setter methods for all fields (id, customer, store, totalPrice, date, orderItems).


    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Store getStore() {
        return store;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
