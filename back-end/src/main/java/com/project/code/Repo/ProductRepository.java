package com.project.code.Repo;

import com.project.code.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 1. Add the repository interface:
    //    - Extend JpaRepository<Product, Long> to inherit basic CRUD functionality.
    //    - This allows the repository to perform operations like save, delete, update, and find without having to implement these methods manually.

    // Example: public interface ProductRepository extends JpaRepository<Product, Long> {}

    // 2. Add custom query methods:
    //    - **findAll**:
    //      - This method will retrieve all products.
    //      - Return type: List<Product>

    // Example: public List<Product> findAll();
    public List<Product> findAll();

    //    - **findByCategory**:
    //      - This method will retrieve products by their category.
    //      - Return type: List<Product>
    //      - Parameter: String category

    // Example: public List<Product> findByCategory(String category);
    public List<Product> findByCategory(String category);

    //    - **findByPriceBetween**:
    //      - This method will retrieve products within a price range.
    //      - Return type: List<Product>
    //      - Parameters: Double minPrice, Double maxPrice

    // Example: public List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    public List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    //    - **findBySku**:
    //      - This method will retrieve a product by its SKU.
    //      - Return type: Product
    //      - Parameter: String sku

    // Example: public Product findBySku(String sku);
    public Product findBySku(String sku);

    //    - **findByName**:
    //      - This method will retrieve a product by its name.
    //      - Return type: Product
    //      - Parameter: String name

    // Example: public Product findByName(String name);
    public Product findByName(String name);

    //    - **findByNameLike**:
    //      - This method will retrieve products by a name pattern for a specific store.
    //      - Return type: List<Product>
    //      - Parameters: Long storeId, String pname
    //      - Use @Query annotation to write a custom query.

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND i.product.category = :category")
    public List<Product> findByNameLike(@Param("storeId") Long storeId, @Param("pname") String pname);

    // findByNameAndCategory: Find products by name and category for a specific store.
    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pname, '%')) AND i.product.category = :category")
    public List<Product> findByNameAndCategory(@Param("storeId") Long storeId, @Param("pname") String pname, @Param("category") String category);

    // findByCategoryAndStoreId: Find products by category for a specific store.
    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND i.product.category = :category")
    public List<Product> findByCategoryAndStoreId(Long storeId, String category);

    // findProductBySubName: Find products by a name pattern (ignoring case).
    @Query("SELECT i FROM Product i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :pname, '%'))")
    public List<Product> findProductBySubName(String pname);

    // findProductsByStoreId: Find all products for a specific store.
    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId")
    public List<Product> findProductsByStoreId(Long storeId);

    // findProductByCategory: Find products by category for a specific store.
    @Query("SELECT i.product FROM Inventory i WHERE i.product.category = :category and i.store.id = :storeId")
    public List<Product> findProductByCategory(String category, Long storeId);

    // findProductBySubNameAndCategory: Find products by a name pattern and category.
    @Query("SELECT i FROM Product i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :pname, '%')) AND i.category = :category")
    public List<Product> findProductBySubNameAndCategory(String pname, String category);
}
