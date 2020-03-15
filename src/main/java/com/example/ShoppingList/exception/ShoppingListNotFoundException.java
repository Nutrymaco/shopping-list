package com.example.ShoppingList.exception;

public class ShoppingListNotFoundException extends RuntimeException {
    public ShoppingListNotFoundException(String id) {
        super("Can't find shopping list with id : " + id);
    }
}
