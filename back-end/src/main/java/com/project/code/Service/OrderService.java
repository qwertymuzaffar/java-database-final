package com.project.code.Service;


import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;


    // 1. **saveOrder Method**:
    //    - Processes a customer's order, including saving the order details and associated items.
    //    - Parameters: `PlaceOrderRequestDTO placeOrderRequest` (Request data for placing an order)
    //    - Return Type: `void` (This method doesn't return anything, it just processes the order)
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {

        // Retrieve or Create the Customer: Check if the customer already exists by their email using findByEmail.
        // If the customer exists, use the existing customer, otherwise, create a new Customer and save it to the repository.
        // Hint: Use customerRepository.findByEmail() to check for an existing customer and customerRepository.save() to save a new customer.
        Customer customer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setName(placeOrderRequest.getCustomerName());
                    newCustomer.setEmail(placeOrderRequest.getCustomerEmail());
                    newCustomer.setPhone(placeOrderRequest.getCustomerPhone());
                    return customerRepository.save(newCustomer);
                });

        // Retrieve the Store: Fetch the store by ID from storeRepository. If the store doesn't exist, throw an exception.
        // Hint: Use storeRepository.findById() to retrieve the store.
        Store store = storeRepository.findById(placeOrderRequest.getStoreId())
                .orElseThrow(() -> new IllegalIdentifierException("Store not found"));

        // Create OrderDetails: Create a new OrderDetails object and set customer, store, total price, and the current datetime.
        // Hint: Set the order date with java.time.LocalDateTime.now() and save the order with orderDetailsRepository.save().
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomer(customer);
        orderDetails.setStore(store);
        orderDetails.setTotalPrice(placeOrderRequest.getTotalPrice());
        orderDetails.setDate(LocalDate.now());

        orderDetailsRepository.save(orderDetails);

        // Create and Save OrderItems: For each product purchased, find the corresponding Inventory, update its stock level, and save the changes.
        // Hint: Use inventoryRepository.findByProductIdandStoreId() to get the inventory and inventoryRepository.save() to update it.
        if (placeOrderRequest.getPurchaseProduct() == null || placeOrderRequest.getPurchaseProduct().isEmpty()) {
            throw new IllegalArgumentException("No items to purchase.");
        }

        for (PurchaseProductDTO item : placeOrderRequest.getPurchaseProduct()) {
            Long productId = item.getId();
            int qty = item.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

            Inventory inventory = inventoryRepository.findByProduct_IdAndStore_Id(productId, store.getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Inventory not found for productId=" + productId + ", storeId=" + store.getId()));

            Integer available = inventory.getStockLevel();
            if (available == null || available < qty) {
                throw new IllegalStateException("Insufficient stock for productId=" + productId +
                        ". Available=" + (available == null ? 0 : available));
            }

            inventory.setStockLevel(available - qty);
            inventoryRepository.save(inventory);

            // Create OrderItem for each product and associate it with the OrderDetails.
            // Hint: Use orderItemRepository.save() to save each order item.
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderDetails(orderDetails);
            orderItem.setProduct(product);
            orderItem.setQuantity(qty);

            Double unitPrice = product.getPrice();
            orderItem.setPrice(unitPrice);

            orderItemRepository.save(orderItem);
        }
    }

    // 2. **Retrieve or Create the Customer**:
    //    - Check if the customer exists by their email using `findByEmail`.
    //    - If the customer exists, use the existing customer; otherwise, create and save a new customer using `customerRepository.save()`.

// 3. **Retrieve the Store**:
//    - Fetch the store by ID from `storeRepository`.
//    - If the store doesn't exist, throw an exception. Use `storeRepository.findById()`.

// 4. **Create OrderDetails**:
//    - Create a new `OrderDetails` object and set customer, store, total price, and the current timestamp.
//    - Set the order date using `java.time.LocalDateTime.now()` and save the order with `orderDetailsRepository.save()`.

// 5. **Create and Save OrderItems**:
//    - For each product purchased, find the corresponding inventory, update stock levels, and save the changes using `inventoryRepository.save()`.
//    - Create and save `OrderItem` for each product and associate it with the `OrderDetails` using `orderItemRepository.save()`.

   
}
