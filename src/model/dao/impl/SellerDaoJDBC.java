package model.dao.impl;

import db.DB;
import db.DbException;
import entities.Department;
import entities.Seller;
import model.dao.SellerDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    Connection connection = null;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller obj) {
        try(PreparedStatement ps =connection.prepareStatement(
                "INSERT INTO seller"
                        + "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
                        +"VALUES"
                        +"(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1,obj.getName());
            ps.setString(2,obj.getEmail());
            ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            ps.setDouble(4, obj.getSalary());
            ps.setInt(5, obj.getDepartment().getId());

            int rows = ps.executeUpdate();

            if(rows>0){
                ResultSet rs = ps.getGeneratedKeys();
                while(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                    System.out.println("Done Id = " +id);
                }
                DB.closeResultSet(rs);
            }else{
                throw new DbException("Nenhuma linha afetada");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Seller obj) {
        try{
            try(PreparedStatement ps = connection.prepareStatement(
                    "UPDATE seller "
                            +"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                            +"WHERE id = ?")){

                ps.setString(1,obj.getName());
                ps.setString(2,obj.getEmail());
                ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
                ps.setDouble(4, obj.getSalary());
                ps.setInt(5, obj.getDepartment().getId());
                ps.setInt(6,obj.getId());

                ps.executeUpdate();

            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Seller obj) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT seller .*,department.Name as DepName " +
                    " FROM seller INNER JOIN department " +
                    " ON seller.DepartmentId = department.Id " +
                    " WHERE seller.Id= ? ");

            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Department dep = instantiantDepartment(rs);
                Seller obj = instantiantSeller(rs, dep);
                return obj;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT seller .*,department.Name as DepName " +
                    " FROM seller INNER JOIN department " +
                    " ON seller.DepartmentId = department.id " +
                    " ORDER BY seller.Name");

            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiantDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller obj = instantiantSeller(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT seller .*,department.Name as DepName " +
                    " FROM seller INNER JOIN department " +
                    " ON seller.DepartmentId = department.id " +
                    "WHERE seller.DepartmentId=? " +
                    " ORDER BY seller.Name");

            st.setInt(1, department.getId());
            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiantDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller obj = instantiantSeller(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Seller instantiantSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setDepartment(dep);
        return obj;
    }

    private Department instantiantDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }
}

