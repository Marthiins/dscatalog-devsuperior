package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests { //Teste de unidade para o service Não vai acessar ao repository que vai acessar o banco
	
	//Como vou me referenciar ao componente service quero testar não posso injetar o @Autowired
		@InjectMocks
		private ProductService service;
		
		@Mock
		private ProductRepository repository;
		
		private long existingId;
		private long nonExistingId;
		
		@BeforeEach
		void setUp() throws Exception {
			existingId = 1L;
			nonExistingId = 1000L;
			
			//Comportamentos para os objetos repositores mocado.
			Mockito.doNothing().when(repository).deleteById(existingId);//Quando eu chamar o deleteById com o id existente o metodo não vai fazer nada
		
			Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);//Lançar excessão quando passar Id que não existe
		}
		
		@Test
		public void deleteShouldDoNothingWhenIdExists() { //deleteNaoFazNadaQuandoIdExist
			
			Assertions.assertDoesNotThrow(() -> {
				service.delete(existingId);
			});
			
			Mockito.verify(repository , Mockito.times(1)).deleteById(existingId);
			
		}
}
