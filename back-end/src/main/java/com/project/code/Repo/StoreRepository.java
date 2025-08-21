package com.project.code.Repo;

import com.project.code.Model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 1. Add the repository interface:
    //    - Extend JpaRepository<Store, Long> to inherit basic CRUD functionality.
    //    - This allows the repository to perform operations like save, delete, update, and find without having to implement these methods manually.

    // Example: public interface StoreRepository extends JpaRepository<Store, Long> {}

    // 2. Add custom query methods:
    //    - **findById**:
    //      - This method will retrieve a store by its ID.
    //      - Return type: Store
    //      - Parameter: Long id

    // Example: public Store findById(Long id);
    public Store findByid(Long id);

    //    - **findBySubName**:
    //      - This method will retrieve stores whose name contains a given substring.
    //      - Return type: List<Store>
    //      - Parameter: String pname
    //      - Use @Query annotation to write a custom query.
    @Query("SELECT s FROM Store s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :pname, '%'))")
    public List<Store> findBySubName(String pname);

    // Similar but without Custom Query
    List<Store> findByNameContainingIgnoreCase(String pname);
}
