package com.project.code.Controller;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    // 1. Set Up the Controller Class:
    //    - Annotate the class with `@RestController` to indicate that this is a REST controller, which handles HTTP requests and responses.
    //    - Use `@RequestMapping("/inventory")` to set the base URL path for all methods in this controller. All endpoints related to inventory will be prefixed with `/inventory`.


    // 2. Autowired Dependencies:
    //    - Autowire necessary repositories and services:
    //      - `ProductRepository` will be used to interact with product data (i.e., finding, updating products).
    //      - `InventoryRepository` will handle CRUD operations related to the inventory.
    //      - `ServiceClass` will help with the validation logic (e.g., validating product IDs and inventory data).

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ServiceClass serviceClass;


    // 3. Define the `updateInventory` Method:
    //    - This method handles HTTP PUT requests to update inventory for a product.
    //    - It takes a `CombinedRequest` (containing `Product` and `Inventory`) in the request body.
    //    - The product ID is validated, and if valid, the inventory is updated in the database.
    //    - If the inventory exists, update it and return a success message. If not, return a message indicating no data available.

    @PutMapping
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request) {

        Product product = request.getProduct();
        Inventory inventory = request.getInventory();

        Map<String, String> map = new HashMap<>();

        if (!serviceClass.validateProductId(product.getId())) {
            map.put("message", "Id " + product.getId() + " not present in database");
            return map;
        }

        productRepository.save(product);

        map.put("message", "Successfully updated product with id: " + product.getId());

        if (inventory != null) {
            try {
                Inventory result = serviceClass.getInventoryId(inventory);
                if (result != null) {
                    inventory.setId(result.getId());
                    inventoryRepository.save(inventory);
                } else {
                    map.put("message", "No data available for this product or store id");
                    return map;
                }
            } catch (DataIntegrityViolationException e) {
                map.put("message", "Error: " + e);
                System.out.println(e);
                return map;
            } catch (Exception e) {
                map.put("message", "Error: " + e);
                System.out.println(e);
                return map;
            }
        }
        return map;
    }

    // 4. Define the `saveInventory` Method:
    //    - This method handles HTTP POST requests to save a new inventory entry.
    //    - It accepts an `Inventory` object in the request body.
    //    - It first validates whether the inventory already exists. If it exists, it returns a message stating so. If it doesn’t exist, it saves the inventory and returns a success message.

    @PostMapping
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {
        Map<String, String> map = new HashMap<>();

        try {
            if (serviceClass.validateInventory(inventory)) {
                inventoryRepository.save(inventory);
            } else {
                map.put("message", "Data Already present in inventory");
                return map;
            }
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        } catch (Exception e) {
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        }
        map.put("message", "Product added to inventory successfully");
        return map;
    }

    // 5. Define the `getAllProducts` Method:
    //    - This method handles HTTP GET requests to retrieve products for a specific store.
    //    - It uses the `storeId` as a path variable and fetches the list of products from the database for the given store.
    //    - The products are returned in a `Map` with the key `"products"`.
    @GetMapping("/{storeId}")
    public Map<String, Object> getAllProducts(@PathVariable Long storeId) {
        Map<String, Object> map = new HashMap<>();

        List<Product> result = productRepository.findProductsByStoreId(storeId);

        map.put("products", result);

        return map;
    }


    // 6. Define the `getProductName` Method:
    //    - This method handles HTTP GET requests to filter products by category and name.
    //    - If either the category or name is `"null"`, adjust the filtering logic accordingly.
    //    - Return the filtered products in the response with the key `"product"`.
    @GetMapping("filter/{category}/{name}/{storeId}")
    public Map<String, Object> getProductName(@PathVariable Long storeId, @PathVariable String category, @PathVariable String name) {

        Map<String, Object> map = new HashMap<>();

        if (category.equals("null")) {
            List<Product> product = productRepository.findByNameLike(storeId, name);
            map.put("product", product);
            return map;
        } else if (name.equals("null")) {
            List<Product> product = productRepository.findByCategoryAndStoreId(storeId, category);
            map.put("product", product);
            return map;
        }

        List<Product> product = productRepository.findByNameAndCategory(storeId, name, category);
        map.put("product", product);
        return map;
    }


    // 7. Define the `searchProduct` Method:
    //    - This method handles HTTP GET requests to search for products by name within a specific store.
    //    - It uses `name` and `storeId` as parameters and searches for products that match the `name` in the specified store.
    //    - The search results are returned in the response with the key `"product"`.
    @GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable String name, @PathVariable Long storeId) {
        Map<String, Object> map = new HashMap<>();

        List<Product> product = productRepository.findByNameLike(storeId, name);

        map.put("product", product);

        return map;
    }


    // 8. Define the `removeProduct` Method:
    //    - This method handles HTTP DELETE requests to delete a product by its ID.
    //    - It first validates if the product exists. If it does, it deletes the product from the `ProductRepository` and also removes the related inventory entry from the `InventoryRepository`.
    //    - Returns a success message with the key `"message"` indicating successful deletion.
    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable Long id) {

        Map<String, String> map = new HashMap<>();

        if (!serviceClass.validateProductId(id)) {
            map.put("message", "Id " + id + " not present in database");
        }

        productRepository.deleteById(id);
        map.put("message", "Successfully deleted product with id: " + id);

        return map;
    }


    // 9. Define the `validateQuantity` Method:
    //    - This method handles HTTP GET requests to validate if a specified quantity of a product is available in stock for a given store.
    //    - It checks the inventory for the product in the specified store and compares it to the requested quantity.
    //    - If sufficient stock is available, return `true`; otherwise, return `false`.
    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable Long quantity, @PathVariable Long storeId, @PathVariable Long productId) {

        Inventory inventory = inventoryRepository.findByProduct_IdAndStore_Id(productId, storeId);

        return inventory.getStockLevel() >= quantity;
    }

}
