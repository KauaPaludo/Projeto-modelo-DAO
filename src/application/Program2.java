package application;

import db.DB;
import entities.Department;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program2 {
    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("FIND BY ID");
        Department dep = departmentDao.findById(1);
        System.out.println(dep);

        System.out.println("DELETE BY ID");
        departmentDao.deleteById(5);
        System.out.println("Delete completed");

        System.out.println("Find All");
        List<Department> list = departmentDao.findAll();
        for(Department d : list){
            System.out.println(d);
        }

        System.out.println("Update");
        Department dep2 = departmentDao.findById(2);
        dep2.setName("Food");
        departmentDao.update(dep2);
        System.out.println("UpDATE COMPLETE");

        Department newDepartment = new Department(null,"Music");
        departmentDao.insert(newDepartment);
        System.out.println("Inserted New id: "+newDepartment.getId());

        DB.closeConnection();
    }
}
