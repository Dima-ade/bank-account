package ro.adela.bank.repository;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import org.hibernate.jpa.HibernatePersistenceProvider;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;

import static org.hibernate.cfg.AvailableSettings.*;

/**
 * https://docs.jboss.org/hibernate/core/3.3/reference/en/html/transactions.html
 *
 * @param <T>
 * @param <K>
 */
public abstract class AbstractRepository<T, K> {

    /*
		If you want to use `persistence.xml`, just rename `src/main/resources/META-INF/persistence.xml.not_used` and replace EntityManagerFactory as below

		protected EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.codspire.db.mgmt");
	 */

    private static final String PERSISTENCE_UNIT_NAME = "ro.adela.bank";

    protected final EntityManagerFactory emf;

    protected AbstractRepository() {
        this.emf = createEntityManagerFactory();
    }

    public static EntityManagerFactory createEntityManagerFactory() {
        jakarta.persistence.spi.PersistenceUnitInfo abc =  archiverPersistenceUnitInfo();
        return new org.hibernate.jpa.HibernatePersistenceProvider().createContainerEntityManagerFactory(abc, config());
    }

    protected AbstractRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    abstract Optional<T> save(T obj);

    abstract Optional<T> findById(K key);

    abstract void delete(T obj);

    public void close() {
        emf.close();
    }

    public static Map<String, Object> config() {
        Map<String, Object> map = new HashMap<>();

        map.put(JPA_JDBC_DRIVER, "org.postgresql.Driver");
        map.put(JPA_JDBC_URL, "jdbc:postgresql://127.0.0.1:5432/accountdb");
        map.put(JPA_JDBC_USER, "postgres");
        map.put(JPA_JDBC_PASSWORD, "admin");
        map.put(DIALECT, org.hibernate.dialect.PostgresPlusDialect.class);
//		map.put(DIALECT, "org.hibernate.dialect.H2Dialect");
        map.put(HBM2DDL_AUTO, "update");
        map.put(SHOW_SQL, "true");
        map.put(QUERY_STARTUP_CHECKING, "false");
        map.put(GENERATE_STATISTICS, "false");
        map.put(USE_REFLECTION_OPTIMIZER, "false");
        map.put(USE_SECOND_LEVEL_CACHE, "false");
        map.put(USE_QUERY_CACHE, "false");
        map.put(USE_STRUCTURED_CACHE, "false");
        map.put(STATEMENT_BATCH_SIZE, "20");
        map.put(AUTOCOMMIT, "false");

        map.put("hibernate.hikari.minimumIdle", "5");
        map.put("hibernate.hikari.maximumPoolSize", "15");
        map.put("hibernate.hikari.idleTimeout", "30000");

        return map;
    }

    public static jakarta.persistence.spi.PersistenceUnitInfo archiverPersistenceUnitInfo() {
        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return PERSISTENCE_UNIT_NAME;
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "com.zaxxer.hikari.hibernate.HikariConnectionProvider";
            }

            @Override
            public jakarta.persistence.spi.PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return null;
            }

            @Override
            public List<String> getMappingFileNames() {
                return Collections.emptyList();
            }

            @Override
            public List<URL> getJarFileUrls() {
                try {
                    return Collections.list(this.getClass()
                            .getClassLoader()
                            .getResources(""));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return null;
            }

            @Override
            public List<String> getManagedClassNames() {
                return Collections.emptyList();
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return new Properties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {

            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };
    }
}