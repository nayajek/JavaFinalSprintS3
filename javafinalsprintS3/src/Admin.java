package src;

public class Admin extends User {

    // Constructor
    public Admin(int id, String username, String password, String email) {
        super(id, username, password, email, "admin");
    }

    // Overloaded constructor without ID (can be useful when creating a new admin)
    public Admin(String username, String password, String email) {
        super(username, password, email, "admin");
    }

    // Displaying the menu specific to admin
    @Override
    public void displayMenu() {
        System.out.println("Admin Menu:");
        System.out.println("1. View All Users");
        System.out.println("2. Delete User");
        System.out.println("3. View All Products");
        System.out.println("4. Logout");
    }

   
}


