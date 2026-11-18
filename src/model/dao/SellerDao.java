package model.dao;

import entities.Department;
import entities.Seller;

import java.util.List;

public interface SellerDao {
    void insert(Seller obj);
    void update(Seller obj);
    void deleteById(Seller obj);
    Department findById(Seller id);
    List<Seller> findAll();
}

