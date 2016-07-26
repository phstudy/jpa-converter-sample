package org.phstudy;


import org.phstudy.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String PERSISTENCE_ECLIPSELINK_UNIT_NAME = "pu-eclipselink";
    private static final String PERSISTENCE_HIBERNATE_UNIT_NAME = "pu-hibernate";

    private static String DATABASE_JDBC_URL = "jdbc:postgresql://%s:%s/%s?stringtype=unspecified";
    private static String DATABASE_USERNAME = "postgres";
    private static String DATABASE_PASSWORD = "postgres";

    private static EntityManagerFactory factory;

    public static void main(String[] args) throws Exception {
        // starting Postgres
        PostgresStarter<PostgresExecutable, PostgresProcess> runtime = PostgresStarter.getDefaultInstance();
        final PostgresConfig config = PostgresConfig.defaultWithDbName("public", DATABASE_USERNAME, DATABASE_PASSWORD);
        PostgresExecutable exec = runtime.prepare(config);
        PostgresProcess process = exec.start();

        DATABASE_JDBC_URL = String.format(DATABASE_JDBC_URL,
                config.net().host(),
                config.net().port(),
                config.storage().dbName()
        );
        initHstore();


        execute(PERSISTENCE_ECLIPSELINK_UNIT_NAME);
        execute(PERSISTENCE_HIBERNATE_UNIT_NAME);

        process.stop();
    }

    private static void execute(String pu) throws Exception {
        factory = Persistence.createEntityManagerFactory(pu, getProperties());

        // persistence context #1
        EntityManager em = factory.createEntityManager();

        em.getTransaction().begin();

        Book book = new Book();
        book.getMetadata().put("title", "'Refactoring: Improving the Design of Existing Code'");
        book.getMetadata().put("author", "Martin Fowler");
        book.getMetadata().put("isdn", "978-0201485677");
        em.persist(book);
        em.getTransaction().commit();

        em.close();


        // persistence context #2 (avoid Level 1 cache)
        em = factory.createEntityManager();

        book = em.find(Book.class, book.getId());
        Map<String, String> metadata = book.getMetadata();
        logger.info("Using {} Persistence Unit",PERSISTENCE_ECLIPSELINK_UNIT_NAME);
        for (Entry<String, String> entry : metadata.entrySet()) {
            logger.info(entry.getKey() + ": " + entry.getValue());
        }

        em.close();

        factory.close();
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("javax.persistence.database-major-version", "9");
        properties.put("javax.persistence.database-minor-version", "5");
        properties.put("javax.persistence.jdbc.url", DATABASE_JDBC_URL);
        properties.put("javax.persistence.jdbc.user", DATABASE_USERNAME);
        properties.put("javax.persistence.jdbc.password", DATABASE_PASSWORD);
        properties.put("javax.persistence.database-product-name", "PostgreSQL");
        properties.put("javax.persistence.schema-generation.database.action", "drop-and-create");
        properties.put("javax.persistence.schema-generation.create-database-schemas", "true");
        return properties;
    }

    private static void initHstore() throws SQLException {
        Connection conn = DriverManager.getConnection(DATABASE_JDBC_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        conn.createStatement().execute("CREATE EXTENSION IF NOT EXISTS hstore;");
        conn.close();
    }
}
