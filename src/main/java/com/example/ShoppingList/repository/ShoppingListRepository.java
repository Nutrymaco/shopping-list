package com.example.ShoppingList.repository;

import com.example.ShoppingList.model.ShoppingList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "shopping_list", path = "shopping-list")
public interface ShoppingListRepository extends MongoRepository<ShoppingList, String> {

}
