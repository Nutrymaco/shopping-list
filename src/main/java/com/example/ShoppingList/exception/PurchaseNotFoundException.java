package com.example.ShoppingList.exception;

public class PurchaseNotFoundException extends RuntimeException{
    public PurchaseNotFoundException(Integer purchaseId, String listId) {
        super("Can't find purchase with id : " + purchaseId + "in shopping list with id : " + listId);
    }
}
