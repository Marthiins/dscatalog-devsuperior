package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

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
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Objeto não encontardo"));
		return new CategoryDTO(entity);
		
	}
	
	

}
