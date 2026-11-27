package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import entities.Department;
import entities.Seller;
import model.dao.DepartmentDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDaoJDBC implements DepartmentDao {

    Connection connection = null;
    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department obj) {
        try(PreparedStatement ps =connection.prepareStatement(
                "INSERT INTO department " +
                        "(Name) " +
                        "VALUES " +
                        "(?)",
                Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1,obj.getName());
            int rows = ps.executeUpdate();

            if(rows>0){
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()){
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
    public void update(Department obj) {
            try(PreparedStatement ps = connection.prepareStatement(
                    "UPDATE department "
                            +"SET Name = ?"
                            +"WHERE id = ?")){

                ps.setString(1,obj.getName());
                ps.setInt(2,obj.getId());

                ps.executeUpdate();

            } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(
                    "SELECT * FROM department WHERE Id = ?");
            st.setInt(1,id);
            rs = st.executeQuery();
            if (rs.next()) {
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                return obj;
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void deleteById(Integer id ) {
        try(PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM department WHERE Id = ?")){

            ps.setInt(1,id);
            ps.executeUpdate();

        }catch (SQLException e){
            throw new DbIntegrityException(e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT * FROM department ORDER BY Name");

            rs = st.executeQuery();
            List<Department> list = new ArrayList<>();
            while (rs.next()) {
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
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
}
