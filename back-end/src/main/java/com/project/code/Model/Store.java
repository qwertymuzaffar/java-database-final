package com.project.code.Model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class Store {

    // 1. Add 'id' field:
    //    - Type: private long
    //    - This field will be auto-incremented.
    //    - Use @Id to mark it as the primary key.
    //    - Use @GeneratedValue(strategy = GenerationType.IDENTITY) to auto-increment it.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 2. Add 'name' field:
    //    - Type: private String
    //    - This field cannot be empty, use the @NotNull annotation to enforce this rule.
    @NotNull(message = "Name cannot be null")
    private String name;

    // 3. Add 'address' field:
    //    - Type: private String
    //    - This field cannot be empty, use the @NotNull and @NotBlank annotations to enforce this rule.
    @NotNull(message = "Address cannot be null")
    @NotBlank
    private String address;

    // 4. Add relationships:
    //    - **Inventory**: A store can have multiple inventory entries.
    //    - Use @OneToMany(mappedBy = "store") to reflect the one-to-many relationship with Inventory.
    //    - Use @JsonManagedReference("inventory-store") to manage bidirectional relationships and avoid circular references.
    @OneToMany(mappedBy = "store")
    @JsonManagedReference("inventory-store")
    private List<Inventory> inventory;

    // 5. Add constructor:
    //    - Create a constructor that accepts name and address as parameters to initialize the Store object.

    public Store() {
    }

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // 6. Add @Entity annotation:
    //    - Use @Entity above the class name to mark it as a JPA entity.

    // 7. Add Getters and Setters:
    //    - Add getter and setter methods for all fields (id, name, address).


    public long getId() {
        return id;
    }

    public @NotNull(message = "Name cannot be null") String getName() {
        return name;
    }

    public @NotNull(message = "Address cannot be null") @NotBlank String getAddress() {
        return address;
    }

    public List<Inventory> getInventory() {
        return inventory;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(@NotNull(message = "Name cannot be null") String name) {
        this.name = name;
    }

    public void setAddress(@NotNull(message = "Address cannot be null") @NotBlank String address) {
        this.address = address;
    }

    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }
}

