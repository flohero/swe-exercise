package swe4.server;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;


public class ConnectionFactory {
    public static final String DB_PROPERTIES = "db.properties";
    private static DataSource dataSource = null;

    public static Connection getConnection() {
        if (dataSource == null) {
            loadMySqlDatasource();
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void loadMySqlDatasource() {
        Properties props = new Properties();
        try {
            props.load(ConnectionFactory.class.getClassLoader().getResourceAsStream(DB_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(props.getProperty("mysql.url"));
        mysqlDataSource.setUser(props.getProperty("mysql.user"));
        mysqlDataSource.setPassword(props.getProperty("mysql.password"));
        dataSource = mysqlDataSource;

    }

}
