package application;

import db.DB;
import entities.Department;
import entities.Seller;
import model.dao.DaoFactory;
import model.dao.SellerDao;

import java.util.*;

public class Program {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Locale.setDefault(Locale.US);

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
        List<Seller> findall = sellerDao.findAll();
        for(Seller fall : findall){
            System.out.println(fall);
        }

        /*Seller newSeller = new Seller(null,"Greg","Greg@gmail.com",new Date(),4500.00,department);
        sellerDao.insert(newSeller);
        System.out.println("New seller "+newSeller.getId());


        seller = sellerDao.findById(1);
        seller.setName("Kaua");
        sellerDao.update(seller);
        System.out.println("Update complete");
        System.out.println(seller);

         */

        System.out.println("Id para delete");
        Integer id = sc.nextInt();
        sellerDao.deleteById(id);
        System.out.println("Delete complete");


        DB.closeConnection();
    }
}
