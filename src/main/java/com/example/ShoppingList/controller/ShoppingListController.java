package com.example.ShoppingList.controller;

import com.example.ShoppingList.assembler.PurchaseAssembler;
import com.example.ShoppingList.exception.PurchaseNotFoundException;
import com.example.ShoppingList.model.Purchase;
import com.example.ShoppingList.model.ShoppingList;
import com.example.ShoppingList.repository.ShoppingListRepository;
import com.example.ShoppingList.exception.ShoppingListNotFoundException;
import com.example.ShoppingList.assembler.ShoppingListAssembler;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController("/api/v1")
public class ShoppingListController {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListAssembler shoppingListAssembler;
    private final PurchaseAssembler  purchaseAssembler;

    public ShoppingListController(ShoppingListRepository repository, ShoppingListAssembler assembler, PurchaseAssembler purchaseAssembler) {
        shoppingListRepository = repository;
        shoppingListAssembler = assembler;
        this.purchaseAssembler = purchaseAssembler;
    }

    @GetMapping("/shopping-lists")
    public CollectionModel<EntityModel<ShoppingList>> allShoppingLists() {
        List<EntityModel<ShoppingList>> shoppingLists = shoppingListRepository
                .findAll()
                .stream()
                .map(shoppingListAssembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(shoppingLists);
    }

    @GetMapping("/shopping-lists/{listId}")
    public EntityModel<ShoppingList> one(@PathVariable String listId) {
        ShoppingList shoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException(listId));
        return shoppingListAssembler.toModel(shoppingList);
    }

    @PostMapping("/shopping-lists")
    ResponseEntity<EntityModel<ShoppingList>> newShoppingLists(@RequestBody ShoppingList shoppingList) {
        EntityModel<ShoppingList> entityModel = shoppingListAssembler.toModel(shoppingListRepository.save(shoppingList));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/shopping-lists/{listId}")
    ResponseEntity<EntityModel<ShoppingList>> putShoppingList(@PathVariable String listId,
                                                                @RequestBody ShoppingList newShoppingList) {

        EntityModel<ShoppingList> entityModel = shoppingListAssembler.toModel(
                shoppingListRepository.findById(listId)
                .map(oldShoppingList -> {
                    oldShoppingList.setName(newShoppingList.getName());
                    oldShoppingList.setPurchaseList(newShoppingList.getPurchaseList());
                    return oldShoppingList;
                        })
                .orElseGet(() ->
                {
                    newShoppingList.setId(listId);
                    return shoppingListRepository.save(newShoppingList);
                })
            );

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/shopping-lists/{listId}")
    ResponseEntity<?> deleteShoppingLists(@PathVariable String listId) {
        shoppingListRepository.deleteById(listId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/shopping-lists/{listId}/purchases")
    public CollectionModel<EntityModel<Purchase>> allShoppingListPurchases(@PathVariable String listId) {
        List<Purchase> purchasesList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException(listId))
                .getPurchaseList();

        List<EntityModel<Purchase>> list = purchasesList
                .stream()
                .map((purchase) -> purchaseAssembler.toModel(purchase, listId))
                .collect(Collectors.toList());

        return new CollectionModel<>(list);
    }

    @GetMapping("/shopping-lists/{listId}/purchases/{purchaseId}")
    public EntityModel<Purchase> oneShoppingListPurchase(@PathVariable("listId") String listId,
                                                         @PathVariable("purchaseId") Integer purchaseId) {
        List<Purchase> purchases = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException(listId))
                .getPurchaseList();

        Purchase purchase = purchases
                .stream()
                .filter((p) -> p.getId().equals(purchaseId))
                .findFirst()
                .orElseThrow(() -> new PurchaseNotFoundException(purchaseId, listId));

        return purchaseAssembler.toModel(purchase, listId);
    }

    @PutMapping("/shopping-lists/{listId}/purchases/{purchaseId}")
    public ResponseEntity<?> putPurchase(@PathVariable("listId") String listId,
                                                            @PathVariable("purchaseId") Integer purchaseId,
                                                            @RequestBody Purchase newPurchase) {
        ShoppingList shoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException(listId));

        List<Purchase> purchases = shoppingList.getPurchaseList();

        Purchase purchase = purchases
                .stream()
                .filter((p) -> p.getId().equals(purchaseId))
                .map((p) -> {
                    p.setName(newPurchase.getName());
                    p.setCount(newPurchase.getCount());
                    p.setDescription(newPurchase.getDescription());
                    shoppingListRepository.save(shoppingList);
                    return p;
                })
                .findFirst()
                .orElseGet(() -> {
                    newPurchase.setId(purchaseId);
                    shoppingList.addPurchase(newPurchase);
                    shoppingListRepository.save(shoppingList);
                    return newPurchase;
                });

        return ResponseEntity
                .created(purchaseAssembler.toModel(purchase, listId).getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(purchase);
    }


    @PostMapping("/shopping-lists/{listId}/purchases")
    ResponseEntity<?> postPurchase(@PathVariable("listId") String listId,
                                                                    @RequestBody Purchase newPurchase) {
        ShoppingList shoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException(listId));

        shoppingList.addPurchase(newPurchase);
        shoppingListRepository.save(shoppingList);

        EntityModel<Purchase> entityModel = purchaseAssembler.toModel(newPurchase, listId);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @DeleteMapping("/shopping-lists/{listId}/purchases/{purchaseId}")
    ResponseEntity<?> deletePurchase(@PathVariable String listId, @PathVariable Integer purchaseId) {
        ShoppingList shoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException(listId));

        Purchase purchase = shoppingList.getPurchaseList()
                .stream()
                .filter(p -> p.getId().equals(purchaseId))
                .findFirst()
                .orElseThrow(() -> new PurchaseNotFoundException(purchaseId, listId));

        shoppingList.getPurchaseList().remove(purchase);
        shoppingListRepository.save(shoppingList);

        return ResponseEntity
                .noContent()
                .build();
    }
}
