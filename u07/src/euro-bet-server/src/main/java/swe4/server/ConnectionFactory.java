package swe4.server;

import com.mysql.cj.jdbc.MysqlDataSource;
import swe4.server.config.DataAccessLayerConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class ConnectionFactory {
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
        Properties props = DataAccessLayerConfig.loadDbProperties();
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(props.getProperty("mysql.url"));
        mysqlDataSource.setUser(props.getProperty("mysql.user"));
        mysqlDataSource.setPassword(props.getProperty("mysql.password"));
        dataSource = mysqlDataSource;
    }

}
