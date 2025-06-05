package ro.adela.bank.test;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBCPDataSourceFactory {

    public static DataSource getDataSource(String dbType){
        Properties props = new Properties();
        InputStream fis = null;
        BasicDataSource ds = new BasicDataSource();

        try {
            fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
            props.load(fis);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        if("postgres".equals(dbType)){
            ds.setDriverClassName(props.getProperty("POSTGRES_DB_DRIVER_CLASS"));
            ds.setUrl(props.getProperty("POSTGRES_DB_URL"));
            ds.setUsername(props.getProperty("POSTGRES_DB_USERNAME"));
            ds.setPassword(props.getProperty("POSTGRES_DB_PASSWORD"));
        }else if("oracle".equals(dbType)){
            ds.setDriverClassName(props.getProperty("ORACLE_DB_DRIVER_CLASS"));
            ds.setUrl(props.getProperty("ORACLE_DB_URL"));
            ds.setUsername(props.getProperty("ORACLE_DB_USERNAME"));
            ds.setPassword(props.getProperty("ORACLE_DB_PASSWORD"));
        }else{
            return null;
        }

        return ds;
    }
}
