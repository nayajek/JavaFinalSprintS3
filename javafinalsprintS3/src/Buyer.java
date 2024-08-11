package src;

public class Buyer extends User {

    // Constructor
    public Buyer(int id, String username, String password, String email) {
        super(id, username, password, email, "buyer");
    }

    // Overloaded constructor without ID (can be useful when creating a new buyer)
    public Buyer(String username, String password, String email) {
        super(username, password, email, "buyer");
    }

    // Displaying the menu specific to the buyer
    @Override
    public void displayMenu() {
        System.out.println("Buyer Menu:");
        System.out.println("1. Browse Products");
        System.out.println("2. Search Products");
        System.out.println("3. View Product Info");
        System.out.println("4. Logout");
    }

    
}


