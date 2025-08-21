package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ServiceClass {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;


    // 1. **validateInventory Method**:
    //    - Checks if an inventory record exists for a given product and store combination.
    //    - Parameters: `Inventory inventory`
    //    - Return Type: `boolean` (Returns `false` if inventory exists, otherwise `true`)
    public boolean validateInventory(Inventory inventory) {
        if (inventory == null || inventory.getProduct() == null || inventory.getStore() == null) {
            throw new IllegalArgumentException("Product and Store are required.");
        }

        Long productId = inventory.getProduct().getId();
        Long storeId  = inventory.getStore().getId();

        Inventory result = inventoryRepository.findByProduct_IdAndStore_Id(productId, storeId);

        return result == null;
    }

    // 2. **validateProduct Method**:
    //    - Checks if a product exists by its name.
    //    - Parameters: `Product product`
    //    - Return Type: `boolean` (Returns `false` if a product with the same name exists, otherwise `true`)
    public boolean validateProduct(Product product) {
        if (product == null || product.getName() == null) {
            throw new IllegalArgumentException("Product name are required.");
        };

        String normalized = product.getName().trim();

        Product result = productRepository.findByName(normalized);

        return result != null;
    }

    // 3. **ValidateProductId Method**:
    //    - Checks if a product exists by its ID.
    //    - Parameters: `long id`
    //    - Return Type: `boolean` (Returns `false` if the product does not exist with the given ID, otherwise `true`)
    public boolean validateProductId(long id) {

        Product result = productRepository.findByid(id);

        return result != null;
    }

    // 4. **getInventoryId Method**:
    //    - Fetches the inventory record for a given product and store combination.
    //    - Parameters: `Inventory inventory`
    //    - Return Type: `Inventory` (Returns the inventory record for the product-store combination)
    public Inventory getInventoryId(Inventory inventory) {

        Long productId = inventory.getProduct().getId();
        Long storeId  = inventory.getStore().getId();

        Inventory result = inventoryRepository.findByProduct_IdAndStore_Id(productId, storeId);

        return result;
    }

}
