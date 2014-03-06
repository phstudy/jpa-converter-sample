package org.phstudy;

import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.phstudy.model.Book;

public class Application {
	static Log logger = LogFactory.getLog(Application.class);

	private static final String PERSISTENCE_UNIT_NAME = "pu";
	private static EntityManagerFactory factory;

	public static void main(String[] args) {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

		// persistence context #1
		EntityManager em = factory.createEntityManager();

		em.getTransaction().begin();
		Book book = new Book();
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

		book = em.find(Book.class, book.getId());
		Map<String, String> metadata = book.getMetadata();
		for (Entry<String, String> entry : metadata.entrySet()) {
			logger.info(entry.getKey() + ": " + entry.getValue());
		}

		em.close();

		factory.close();
	}
}
