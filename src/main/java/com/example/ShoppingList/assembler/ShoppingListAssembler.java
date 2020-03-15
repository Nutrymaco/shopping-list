package com.example.ShoppingList.assembler;

import com.example.ShoppingList.model.ShoppingList;
import com.example.ShoppingList.controller.ShoppingListController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ShoppingListAssembler implements RepresentationModelAssembler<ShoppingList, EntityModel<ShoppingList>> {
    @Override
    public EntityModel<ShoppingList> toModel(ShoppingList entity) {
        return new EntityModel<>(entity,
                WebMvcLinkBuilder.linkTo(methodOn(ShoppingListController.class).one(entity.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(ShoppingListController.class).allShoppingLists()).withRel("shoppinglists"));
    }
}
