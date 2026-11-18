package application;

import entities.Department;
import entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args){

        Department dp = new Department(1,"Carros");
        Seller seller = new Seller(1,"kaua","kauapaludo2@gmail.com",new Date(), 3000.00, dp);
        System.out.println(dp);
        System.out.println(seller);
    }
}
