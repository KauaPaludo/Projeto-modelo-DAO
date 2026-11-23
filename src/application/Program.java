package application;

import db.DB;
import entities.Department;
import entities.Seller;
import model.dao.DaoFactory;
import model.dao.SellerDao;

import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args){

        Department dp = new Department(1,"Carros");
        System.out.println(dp);
        SellerDao sellerDao = DaoFactory.createSellerDao();

        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        Department department = new Department(1,null);
        List<Seller>list = sellerDao.findByDepartment(department);
        for(Seller s: list){
            System.out.println(s);
        }

        DB.closeConnection();
    }
}
