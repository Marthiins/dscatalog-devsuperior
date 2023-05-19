package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;



@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository CategoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable){ //Metodo acessar o repository e acessar la no banco de dados a Product
		Page<Product> list = repository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));
				
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj =  repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Objeto não encontardo"));
		return new ProductDTO(entity , entity.getCategories());
		
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product(); //CovnerterDTO para a entidade Product
		copyDtoToEntity(dto, entity); //Chamando o metodo que foi criado
		
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try{
			Product entity = repository.getOne(id); //Atualizar registro na JPA tem que instanciar
			copyDtoToEntity(dto, entity);
			
			entity = repository.save(entity); 
			return new ProductDTO(entity); //retornando a entidade convertida para DTO
		}
		catch(EntityNotFoundException e) { //Salvar excessão do caso getOne não existir
			throw new ResourceNotFoundException("Id não encontrado" + id);
			
		}
	
	}

	
	public void delete(Long id) {
			//findById(id);
		try {  
				repository.deleteById(id);
				
		}	
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado" + id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Não é possivel excluir um produto violação de integridade.");
		}
	}

	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();//Carregar as categorias do DTO para entidades
		for(CategoryDTO catDto : dto.getCategories()) { //forech para percorrer todas as CategoryDTO que estão associados ao meu dto
			Category category = CategoryRepository.getOne(catDto.getId());//Instanciar uma entidade de categoria pelo JPA
			entity.getCategories().add(category);
		}
		
	}


}
