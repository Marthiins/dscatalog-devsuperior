package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests { //Teste de unidade para o service Não vai acessar ao repository que vai acessar o banco
	
	//Como vou me referenciar ao componente service quero testar não posso injetar o @Autowired
		@InjectMocks
		private ProductService service;
		
		@Mock
		private ProductRepository repository;
		
		private long existingId;
		private long nonExistingId;
		private long dependentId;
		
		@BeforeEach
		void setUp() throws Exception {
			existingId = 1L;
			nonExistingId = 1000L;
			dependentId = 4L;
			
			//Comportamentos para os objetos repositores mocado.
			Mockito.doNothing().when(repository).deleteById(existingId);//Quando eu chamar o deleteById com o id existente o metodo não vai fazer nada
			
			Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);//Lançar excessão quando passar Id que não existe
		
			doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
		}
		
		
		@Test
		public void deleteShoulDatabaseExceptionWhenDependentId() { //deve lançar essa exceção caso deletar um produto que depende do Id
			
			Assertions.assertThrows(DatabaseException.class, () -> {
				service.delete(dependentId);
			});
			
			verify(repository , times(1)).deleteById(dependentId);
			
		}
		
		@Test
		public void deleteShoulResourceNotFoundExceptionWhenIdNotExists() { //deve lançar essa exceção do controller quando o Id não existir
			
			Assertions.assertThrows(ResourceNotFoundException.class, () -> {
				service.delete(nonExistingId);
			});
			
			Mockito.verify(repository , Mockito.times(1)).deleteById(nonExistingId);
			
		}
		
		@Test
		public void deleteShouldDoNothingWhenIdExists() { //deleteNaoFazNadaQuandoIdExist
			
			Assertions.assertDoesNotThrow(() -> {
				service.delete(existingId);
			});
			
			Mockito.verify(repository , Mockito.times(1)).deleteById(existingId);
			
		}
}
