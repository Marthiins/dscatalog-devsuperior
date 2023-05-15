package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;


	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){ //Metodo acessar o repository e acessar la no banco de dados a Category
		List<Category> list = repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x))
				.collect(Collectors.toList());//Convertendo lista de categoria para CategoriaDTO com lambda
	
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj =  repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Objeto não encontardo"));
		return new CategoryDTO(entity);
		
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category(); //CovnerterDTO para a entidade Category
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try{
			Category entity = repository.getOne(id); //Atualizar registro na JPA tem que instanciar
			entity.setName(dto.getName());	//Atualizei os dados na memoria
			entity = repository.save(entity); 
			return new CategoryDTO(entity); //retornando a entidade convertida para DTO
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
			throw new DatabaseException("Não é possivel excluir uma categoria violação de integridade.");
		}
	}	

}
