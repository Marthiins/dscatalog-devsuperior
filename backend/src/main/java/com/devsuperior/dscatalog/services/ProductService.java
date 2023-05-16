package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;


	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPage(PageRequest pageRequest){ //Metodo acessar o repository e acessar la no banco de dados a Product
		Page<Product> list = repository.findAll(pageRequest);
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
		//entity.setName(dto.getName());
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try{
			Product entity = repository.getOne(id); //Atualizar registro na JPA tem que instanciar
			//entity.setName(dto.getName());	//Atualizei os dados na memoria
			entity = repository.save(entity); 
			return new ProductDTO(entity); //retornando a entidade convertida para DTO
		}
		catch(EntityNotFoundException e) { //Salvar excessão do caso getOne não existir
			throw new ResourceNotFoundException("Id não encontrado" + id);
			
		}
	
	}

	public void delete(Long id) {
			findById(id);
		try {  //Excessão deletar caso id não exista
			repository.deleteById(id);
		}	
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado" + id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Não é possivel excluir um produto  violação de integridade.");
		}
	}	

}
