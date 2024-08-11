package src;

import java.util.ArrayList;
import java.util.List;


public class Seller extends User {

    private List<Product> products;  // List of products that the seller has listed

    // Constructor
    public Seller(int id, String username, String password, String email) {
        super(id, username, password, email, "seller");
        this.products = new ArrayList<>();
    }

    // Overloaded constructor without ID (can be useful when creating a new seller)
    public Seller(String username, String password, String email) {
        super(username, password, email, "seller");
        this.products = new ArrayList<>();
    }

    // Adding a product to the seller's list of products
    public void addProduct(Product product) {
        products.add(product);
    }

    // Getting the list of products
    public List<Product> getProducts() {
        return products;
    }

    // Displaying the seller-specific menu
    @Override
    public void displayMenu() {
        System.out.println("Seller Menu:");
        System.out.println("1. Add Product");
        System.out.println("2. View My Products");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("5. Logout");
    }

   
}

