package application;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Cart {
    private TreeMap<Integer, TreeMap<String, CartEntry>> aisleMap;
    private static Cart INSTANCE;

    public static Cart getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Cart();
        }
        return INSTANCE;
    }

    public Cart() {
        this.aisleMap = new TreeMap<>();
    }

    public void addProduct(String productName) throws FileNotFoundException {
        Product prod = fetchProduct(productName);
        Integer aisle = prod.getAisle();
        TreeMap<String, CartEntry> countMap = aisleMap.computeIfAbsent(aisle, k -> new TreeMap<>());
        CartEntry entry = countMap.get(productName);
        if (entry != null) {
            entry.increaseQuantity();
        } else {
            CartEntry newEntry = new CartEntry(prod, 1);
            countMap.put(productName, newEntry);
        }
    }

    public void deleteProduct(String productName) throws FileNotFoundException {
        Product prod = fetchProduct(productName);
        Integer aisle = prod.getAisle();
        TreeMap<String, CartEntry> countMap = aisleMap.get(aisle);
        if (countMap != null) {
            CartEntry entry = countMap.get(productName);
            if (entry != null) {
                entry.decreaseQuantity();
                if (entry.getQuantity() == 0) {
                    countMap.remove(productName);
                    if (countMap.isEmpty()) {
                        aisleMap.remove(aisle);
                    }
                }
            }
        }
    }

    public Product fetchProduct(String productName) throws FileNotFoundException {
        List<Product> availableProds = ReadingJson.ReadingJson();  // Replace this method if needed.
        for (Product prod : availableProds) {
            if (prod.getName().equals(productName)) {
                return prod;
            }
        }
        return null;  // Consider throwing an exception or specific handling if product is not found.
    }

    public int getQuantity(String productName) throws FileNotFoundException {
        Product prod = fetchProduct(productName);
        Integer aisle = prod.getAisle();
        TreeMap<String, CartEntry> countMap = aisleMap.get(aisle);
        if (countMap != null) {
            CartEntry entry = countMap.get(productName);
            if (entry != null) {
                return entry.getQuantity();
            }
        }
        return 0;
    }

    public double calculateTotal() {
        double total = 0;
        for (TreeMap<String, CartEntry> countMap : aisleMap.values()) {
            for (CartEntry entry : countMap.values()) {
                total += entry.getProduct().getPrice() * entry.getQuantity();
            }
        }
        return total;
    }

    public List<Integer> getAisles() {
        return new ArrayList<>(aisleMap.keySet());
    }

    public List<String> getProductNames(Integer aisle) {
        if (aisleMap.containsKey(aisle)) {
            return new ArrayList<>(aisleMap.get(aisle).keySet());
        }
        return new ArrayList<>();
    }

    public TreeMap<Integer, TreeMap<String, CartEntry>> getAisleMap() {
        return this.aisleMap;
    }

    public TreeMap<String, CartEntry> getCountMap(Integer aisle) {
        return this.aisleMap.getOrDefault(aisle, new TreeMap<>());
    }
    
    public List<CartEntry> getCartItems() {
        List<CartEntry> allItems = new ArrayList<>();
        for (TreeMap<String, CartEntry> aisleItems : aisleMap.values()) {
            allItems.addAll(aisleItems.values());
        }
        return allItems;
    }
}