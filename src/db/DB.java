package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

    //CONECTAR COM O BANCO DE DADOS
    private static Connection connection = null;
    public static Connection getConnection(){
        if(connection==null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                connection = DriverManager.getConnection(url, props);
            }
            catch (SQLException e){
                throw new DbException(e.getMessage());
            }
        }
        return connection;
    }

    //FECHAR A CONEXÃO
    public static void closeConnection(){
        if(connection!=null){
            try {
                connection.close();
            }
            catch (SQLException e){
                throw new DbException(e.getMessage());
            }
        }
    }

    //CARREGAR AS INFORMAÇÕES DO BANCO DO ARQUIVO DB.PROPERTIES
    private static Properties loadProperties(){
        try(FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        }
        catch (IOException e){
            throw new DbException(e.getMessage());
        }
    }

}
