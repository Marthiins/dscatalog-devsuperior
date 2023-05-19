package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

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
		private PageImpl<Product> page;
		private Product product;
		
		@BeforeEach
		void setUp() throws Exception {
			existingId = 1L;
			nonExistingId = 1000L;
			dependentId = 4L;
			product = Factory.createProduct();
			page = new PageImpl<>(List.of(product)); //Já instanciado com uma lista com um unico elemento que vai ser o produto
			
			
			//Quando o metodo tem retorno primeira faz a ação <When><Cenário quando acontecer isso> e depois a  <should><AÇÃO>
			when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
			
			when(repository.save(ArgumentMatchers.any())).thenReturn(product);
			
			when(repository.findById(existingId)).thenReturn(Optional.of(product)); //Instanciando um optional que tem um produto
			when(repository.findById(nonExistingId)).thenReturn(Optional.empty()); //Instanciando um optional vazio
			
			
			
			//Comportamentos para os objetos repositores mocado. quando o metodo é void <should><AÇÃO> e depois o <When><Cenário quando acontecer isso>
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
