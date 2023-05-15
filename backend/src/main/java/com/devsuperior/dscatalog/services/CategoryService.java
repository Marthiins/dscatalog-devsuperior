package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;


	public List<CategoryDTO> findAll(){ //Metodo acessar o repository e acessar la no banco de dados a Category
		List<Category> list = repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x))
				.collect(Collectors.toList());//Convertendo lista de categoria para CategoriaDTO com lambda
	
	}

}
