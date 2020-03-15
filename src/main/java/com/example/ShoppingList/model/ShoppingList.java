package com.example.ShoppingList.model;

import lombok.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Document
public class ShoppingList {
    @Id
    private String id;

    private String name;

    private List<Purchase> purchaseList;

    public ShoppingList(String name, List<Purchase> purchaseList) {
        this.name = name;
        setPurchaseList(purchaseList);
    }

    public void addPurchase(Purchase purchase) {
        purchase.setId(purchaseList.size());
        purchaseList.add(purchase);
    }


    public void setPurchaseList(List<Purchase> purchaseList) {
        AtomicReference<Integer> id = new AtomicReference<>(0);

        this.purchaseList = purchaseList
                .stream()
                .peek(p -> {
                    p.setId(id.getAndSet(id.get() + 1));
                })
                .collect(Collectors.toList());

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ShoppingList) {
            return id.equals(((ShoppingList) obj).getId()) && name.equals(((ShoppingList) obj).getName())
                    && purchaseList.equals(((ShoppingList) obj).getPurchaseList());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode() + purchaseList.hashCode();
    }
}
