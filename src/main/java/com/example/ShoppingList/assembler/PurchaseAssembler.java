package com.example.ShoppingList.assembler;

import com.example.ShoppingList.model.Purchase;
import com.example.ShoppingList.controller.ShoppingListController;
import org.springframework.data.rest.core.mapping.SupportedHttpMethods;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PurchaseAssembler implements RepresentationModelAssembler<Purchase, EntityModel<Purchase>> {

    public EntityModel<Purchase> toModel(Purchase entity, String shopListId) {
        return new EntityModel<>(entity,
                WebMvcLinkBuilder.
                        linkTo(methodOn(ShoppingListController.class).
                                oneShoppingListPurchase(shopListId, entity.getId())).withSelfRel(),
                WebMvcLinkBuilder.
                        linkTo(methodOn(ShoppingListController.class).
                                allShoppingListPurchases(shopListId)).withRel("purchases"));
    }

    @Override
    public EntityModel<Purchase> toModel(Purchase entity) {
        return null;
    }
}
