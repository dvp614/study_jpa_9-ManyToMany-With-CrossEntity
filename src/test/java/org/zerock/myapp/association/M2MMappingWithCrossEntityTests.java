package org.zerock.myapp.association;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.zerock.myapp.entity.Product3;
import org.zerock.myapp.entity.Shopper3;
import org.zerock.myapp.entity.ShopperProduct;
import org.zerock.myapp.entity.ShopperProductId;
import org.zerock.myapp.util.PersistenceUnits;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class M2MMappingWithCrossEntityTests {
	private EntityManagerFactory emf;
	private EntityManager em;

	@BeforeAll
	void beforeAll() {
		log.trace("beforeAll() invoked.");

		this.emf = Persistence.createEntityManagerFactory(PersistenceUnits.H2);

		this.em = this.emf.createEntityManager();
		assertNotNull(this.em);
	} // beforeAll

	@AfterAll
	void afterAll() {
		log.trace("afterAll() invoked.");

		this.em.clear();
		try {this.em.close();} catch (Exception _ignored) {}
		try {this.emf.close();} catch (Exception _ignored) {}
	} // afterAll

	@Disabled
	@Order(1)
	@Test
//	@RepeatedTest(1)
	@DisplayName("1. prepareData")
	@Timeout(value = 5L, unit = TimeUnit.MINUTES)
	void prepareData() {
		log.trace("prepareData invoked.");

		IntStream.rangeClosed(1, 7).forEachOrdered(seq -> {

			try {
				this.em.getTransaction().begin();

				// 엔티티.....
				Product3 transientProduct = new Product3();
				transientProduct.setName("NAME-" + seq);

				this.em.persist(transientProduct);

				this.em.getTransaction().commit();
			} catch (Exception e) {
				this.em.getTransaction().rollback();

				throw e;
			} // try-catch
		}); // forEachOrdered

		Product3 foundProduct1 = this.em.find(Product3.class, 1L);
		Product3 foundProduct2 = this.em.find(Product3.class, 2L);
		Product3 foundProduct3 = this.em.find(Product3.class, 3L);
		Product3 foundProduct4 = this.em.find(Product3.class, 4L);
		Product3 foundProduct5 = this.em.find(Product3.class, 5L);
		Product3 foundProduct6 = this.em.find(Product3.class, 6L);
		Product3 foundProduct7 = this.em.find(Product3.class, 7L);

		Objects.requireNonNull(foundProduct1);
		Objects.requireNonNull(foundProduct2);
		Objects.requireNonNull(foundProduct3);
		Objects.requireNonNull(foundProduct4);
		Objects.requireNonNull(foundProduct5);
		Objects.requireNonNull(foundProduct6);
		Objects.requireNonNull(foundProduct7);

//
//		// 3.
		LongStream.of(1L, 2L, 3L).forEachOrdered(seq -> {

			try {
				this.em.getTransaction().begin();

				// 엔티티.....
				Shopper3 transientShopper = new Shopper3();
				transientShopper.setId(seq);
				transientShopper.setName("NAME-" + seq);

				this.em.persist(transientShopper);

				this.em.getTransaction().commit();
			} catch (Exception e) {
				this.em.getTransaction().rollback();
				throw e;
			} // try-catch

		}); // forEachOrdered

		Shopper3 foundShopper1 = this.em.find(Shopper3.class, 1L);
		Shopper3 foundShopper2 = this.em.find(Shopper3.class, 2L);
		Shopper3 foundShopper3 = this.em.find(Shopper3.class, 3L);

		Objects.requireNonNull(foundShopper1);
		Objects.requireNonNull(foundShopper2);
		Objects.requireNonNull(foundShopper3);

		try {
			this.em.getTransaction().begin();
			
			IntStream.rangeClosed(1, 17).forEachOrdered(seq -> {
				ShopperProduct transientOrder = new ShopperProduct();
				transientOrder.setAmount(seq);

				switch (seq) {
					case 1, 3, 5, 7, 9, 11, 12 -> {
						transientOrder.setShopper(foundShopper1);
						foundShopper1.getShopperProducts().add(transientOrder);
	
						if (seq == 1)
							transientOrder.setProduct(foundProduct1);
						if (seq == 3)
							transientOrder.setProduct(foundProduct3);
						if (seq == 5)
							transientOrder.setProduct(foundProduct5);
						if (seq == 7)
							transientOrder.setProduct(foundProduct7);
						if (seq == 9)
							transientOrder.setProduct(foundProduct2);
						if (seq == 11)
							transientOrder.setProduct(foundProduct6);
						if (seq == 12)
							transientOrder.setProduct(foundProduct4);
	
					}
					case 2, 4, 6, 8, 15 -> {
						transientOrder.setShopper(foundShopper2);
						foundShopper2.getShopperProducts().add(transientOrder);
	
						if (seq == 2)
							transientOrder.setProduct(foundProduct1);
						if (seq == 4)
							transientOrder.setProduct(foundProduct3);
						if (seq == 6)
							transientOrder.setProduct(foundProduct7);
						if (seq == 8)
							transientOrder.setProduct(foundProduct2);
						if (seq == 15)
							transientOrder.setProduct(foundProduct6);
					}
					case 10, 13, 14, 16, 17 -> {
						transientOrder.setShopper(foundShopper3);
						foundShopper3.getShopperProducts().add(transientOrder);
	
						if (seq == 10)
							transientOrder.setProduct(foundProduct1);
						if (seq == 13)
							transientOrder.setProduct(foundProduct3);
						if (seq == 14)
							transientOrder.setProduct(foundProduct7);
						if (seq == 16)
							transientOrder.setProduct(foundProduct2);
						if (seq == 17)
							transientOrder.setProduct(foundProduct4);
					}
				} // switch

				this.em.persist(transientOrder);
			}); // .forEachOrdered
			this.em.getTransaction().commit();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw e;
		} // try-catch

	} // prepareData

//	@Disabled
	@Order(2)
	@Test
//	@RepeatedTest(1)
	@DisplayName("2. testObjectGraph1")
	@Timeout(value = 5L, unit = TimeUnit.MINUTES)
	void testObjectGraph1() {
		log.trace("testObjectGraph1 invoked.");
		
		var shopperId = new Random().nextLong(1L, 3L);
		
		Shopper3 foundShopper = this.em.find(Shopper3.class, shopperId);
		Objects.nonNull(foundShopper);
		
		foundShopper.getShopperProducts().forEach(sp -> log.info(sp.toString()));
	} // testObjectGraph1
	
//	@Disabled
	@Order(3)
	@Test
//	@RepeatedTest(1)
	@DisplayName("3. testObjectGraph2")
	@Timeout(value = 5L, unit = TimeUnit.MINUTES)
	void testObjectGraph2() {
		log.trace("testObjectGraph2 invoked.");
		
		var shopperId = new Random().nextLong(1L, 3L);
		var productId = new Random().nextLong(1L, 17L);
		
		ShopperProductId compositeKey = new ShopperProductId();
		compositeKey.setShopper(shopperId);
		compositeKey.setProduct(productId);
		
		
		
		ShopperProduct 주문내역 = this.em.find(ShopperProduct.class, compositeKey);

		log.info("\t+ 주문내역 : {}", 주문내역);
	} // testObjectGraph2
	
//	@Disabled
	@Order(4)
	@Test
//	@RepeatedTest(1)
	@DisplayName("4. testObjectGraph3")
	@Timeout(value = 5L, unit = TimeUnit.MINUTES)
	void testObjectGraph3() {
		log.trace("testObjectGraph2 invoked.");
		
		var productId = new Random().nextLong(1L, 17L);
		
		Product3 주문내역 = this.em.find(Product3.class, productId);
		Objects.nonNull(주문내역);

		log.info("\t+ 주문내역 : {}", 주문내역);
	} // testObjectGraph3

} // end class
