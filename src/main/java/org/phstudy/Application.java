package org.phstudy;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.phstudy.model.IBook;

public class Application {
	static Log logger = LogFactory.getLog(Application.class);

	private static final String PERSISTENCE_ECLIPSELINK_UNIT_NAME = "pu-eclipselink";
	private static final String PERSISTENCE_HIBERNATE_UNIT_NAME = "pu-hibernate";
	
	private static final String DATABASE_JDBC_URL = "jdbc:postgresql:DATABASE_NAME?stringtype=unspecified";
	private static final String DATABASE_USERNAME = "DATABASE_USERNAME";
	private static final String DATABASE_PASSWORD = "DATABASE_PASSWORD";

	private static EntityManagerFactory factory;

	public static void main(String[] args) throws Exception {
		execute(PERSISTENCE_ECLIPSELINK_UNIT_NAME);
		execute(PERSISTENCE_HIBERNATE_UNIT_NAME);
	}

	private static void execute(String pu) throws Exception {
		factory = Persistence.createEntityManagerFactory(pu, getProperties());

		// persistence context #1
		EntityManager em = factory.createEntityManager();

		em.getTransaction().begin();

		Class<?> bookClz = null;
		switch (pu) {
		case PERSISTENCE_ECLIPSELINK_UNIT_NAME:
			bookClz = org.phstudy.eclipselink.model.Book.class;
			break;
		case PERSISTENCE_HIBERNATE_UNIT_NAME:
			bookClz = org.phstudy.hibernate.model.Book.class;
			break;
		}

		IBook book = (IBook) bookClz.newInstance();

		book.getMetadata()
				.put("title",
						"'Refactoring:\\ Improving\\ the\\ Design\\ of\\ Existing\\ Code'");
		book.getMetadata().put("author", "Martin\\ Fowler");
		book.getMetadata().put("isdn", "978-0201485677");
		em.persist(book);
		em.getTransaction().commit();

		em.close();

		// persistence context #2
		em = factory.createEntityManager();

		book = (IBook) em.find(bookClz, book.getId());
		Map<String, String> metadata = book.getMetadata();
		for (Entry<String, String> entry : metadata.entrySet()) {
			logger.info(entry.getKey() + ": " + entry.getValue());
		}

		em.close();

		factory.close();
	}

	private static Properties getProperties() {
		Properties properties = new Properties();
		properties
				.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		properties.put("javax.persistence.database-major-version", "9");
		properties.put("javax.persistence.database-minor-version", "3");
		properties.put("javax.persistence.jdbc.url", DATABASE_JDBC_URL);
		properties.put("javax.persistence.jdbc.user", DATABASE_USERNAME);
		properties.put("javax.persistence.jdbc.password", DATABASE_PASSWORD);
		properties.put("javax.persistence.database-product-name", "PostgreSQL");
		properties.put("javax.persistence.schema-generation.database.action",
				"drop-and-create");
		properties.put(
				"javax.persistence.schema-generation.create-database-schemas",
				"true");
		return properties;
	}
}
