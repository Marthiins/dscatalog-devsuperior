package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	//Testando os metodos do Repository
	@Autowired
	private ProductRepository repository;
	
	private long existeId;
	private long nonExistingId;
	private long countTotalProducts;
	
	//SERA EXECUTADO (BEFORE EACH / ANTES DE CADA TESTE)
	@BeforeEach
	void setUp() throws Exception {
		existeId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25;
	}
	
	@Test
	void savePersistAutoIncrementQuandoIdIsNulo() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
		
	}

	@Test
	public void deleteObjetoQuandoIdExixte() {
		
		repository.deleteById(existeId);
		
		Optional<Product> result = repository.findById(existeId);//declarar o que tem que acontecer
		Assertions.assertFalse(result.isPresent());//isPresent testa se existe um objeto dentro do optional
	}
	
	@Test
	public void deleteEmptyResultDataAccessExceptionQuandoIdNaoExistir() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
					
		});
	
	}
}
