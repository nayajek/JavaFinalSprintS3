package src;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static ProductService productService;

    public static void main(String[] args) {
        try {
            userService = new UserService();  // Initializing UserService
            productService = new ProductService();  // Initializing ProductService
            System.out.println("Welcome to the E-Commerce Platform!");
            mainMenuLoop();
        } catch (SQLException e) {
            System.out.println("Error initializing the application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mainMenuLoop() throws SQLException {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerUser() throws SQLException {
        System.out.println("\nRegister New User:");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter role (buyer/seller/admin): ");
        String role = scanner.nextLine().toLowerCase();

        User user;
        switch (role) {
            case "buyer":
                user = new Buyer(username, password, email);
                break;
            case "seller":
                user = new Seller(username, password, email);
                break;
            case "admin":
                user = new Admin(username, password, email);
                break;
            default:
                System.out.println("Invalid role. Registration failed.");
                return;
        }

        boolean isRegistered = userService.registerUser(user);
        if (isRegistered) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed!");
        }
    }

    private static void loginUser() throws SQLException {
        System.out.println("\nLogin:");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userService.login(username, password);
        if (user != null) {
            System.out.println("Login successful! Welcome, " + user.getUsername() + "!");
            displayRoleMenu(user);
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
    }

    private static void displayRoleMenu(User user) {
        while (true) {
            user.displayMenu();
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  

            switch (user.getRole()) {
                case "buyer":
                    handleBuyerActions(choice, (Buyer) user);
                    break;
                case "seller":
                    handleSellerActions(choice, (Seller) user);
                    break;
                case "admin":
                    handleAdminActions(choice, (Admin) user);
                    break;
                default:
                    System.out.println("Invalid role.");
                    return;
            }

            if (choice == 4 || choice == 5) {
                break;
            }
        }
    }

    private static void handleSellerActions(int choice, Seller seller) {
        switch (choice) {
            case 1:
                System.out.println("Adding a new product...");
                addProduct(seller);
                break;
            case 2:
                System.out.println("Viewing all your products...");
                viewSellerProducts(seller);
                break;
            case 3:
                System.out.println("Updating a product...");
                updateProduct(seller);
                break;
            case 4:
                System.out.println("Deleting a product...");
                deleteProduct(seller);
                break;
            case 5:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    // Example implementations of the methods used in handleSellerActions
    
    private static void addProduct(Seller seller) {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product description: ");
        String description = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
    
        System.out.print("Enter product quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        Product product = new Product(name, description, price, quantity, seller.getId());
        boolean success = productService.addProduct(product);
        if (success) {
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Failed to add product.");
        }
    }
    
    private static void viewSellerProducts(Seller seller) {
        try {
            List<Product> products = productService.getProductsBySeller(seller.getId());
    
            if (products.isEmpty()) {
                System.out.println("You have no products listed.");
            } else {
                System.out.println("Your Products:");
                for (Product product : products) {
                    System.out.println("ID: " + product.getId() + ", Name: " + product.getName() +
                                       ", Description: " + product.getDescription() + 
                                       ", Price: $" + product.getPrice());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void updateProduct(Seller seller) {
        System.out.print("Enter the ID of the product to update: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        System.out.print("Enter new product name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new product description: ");
        String newDescription = scanner.nextLine();
        System.out.print("Enter new product price: ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
    
        System.out.print("Enter new product quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        Product updatedProduct = new Product(productId, newName, newDescription, newPrice, newQuantity, seller.getId());
        boolean success = productService.updateProduct(updatedProduct);
        if (success) {
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Failed to update product.");
        }
    }
    
    private static void deleteProduct(Seller seller) {
        System.out.print("Enter the ID of the product to delete: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        boolean success = productService.deleteProduct(productId);
        if (success) {
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("Failed to delete product.");
        }
    }
    

    private static void handleBuyerActions(int choice, Buyer user) {
        switch (choice) {
            case 1:
                System.out.println("Browsing products...");
                // Implementing product browsing logic
                browseProducts(user);
                break;
            case 2:
                System.out.println("Searching products...");
                // Implementing product search logic
                searchProducts(user);
                break;
            case 3:
                System.out.println("Viewing product info...");
                // Implementing product info viewing logic
                viewProductInfo(user);
                break;
            case 4:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
        

    private static void viewProductInfo(Buyer buyer) {
        System.out.print("Enter the ID of the product to view: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); 
    
        try {
            Product product = productService.getProductById(productId);
    
            if (product != null) {
                System.out.println("Product Details:");
                System.out.println("ID: " + product.getId());
                System.out.println("Name: " + product.getName());
                System.out.println("Description: " + product.getDescription());
                System.out.println("Price: $" + product.getPrice());
                System.out.println("Seller ID: " + product.getSellerId());
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving product details: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private static void searchProducts(Buyer buyer) {
        // implementing the logic for the buyer to search for specific products.
        System.out.print("Enter the name of the product to search for: ");
        String productName = scanner.nextLine();
        System.out.println("Searching for products named: " + productName);
        
    }

    private static void browseProducts(Buyer buyer) {
        // implementing the logic for the buyer to browse available products.
        System.out.println("Displaying all available products...");
        
    }

    private static void handleAdminActions(int choice, Admin admin) {
        switch (choice) {
            case 1:
                System.out.println("Viewing all users...");
                // Implementing logic to view all users
                viewAllUsers(admin);
                break;
            case 2:
                System.out.println("Deleting a user...");
                // Implementing logic to delete a user
                deleteUser(admin);
                break;
            case 3:
                System.out.println("Viewing all products...");
                // Implementing logic to view all products
                viewAllProducts(admin);
                break;
            case 4:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void viewAllUsers(Admin admin) {
    try {
        // Fetching all users from the database using UserService
        List<User> users = userService.getAllUsers();

        // Checking if users were found
        if (users.isEmpty()) {
            System.out.println("No users found in the system.");
        } else {
            System.out.println("All registered users:");
            // Displaying each user
            for (User user : users) {
                System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername() + 
                                   ", Email: " + user.getEmail() + ", Role: " + user.getRole());
            }
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving users: " + e.getMessage());
        e.printStackTrace();
    }
}


    private static void viewAllProducts(Admin admin) {
        //implementing the logic for the admin to view all products.
        System.out.println("Fetching all products from the database...");
        
    }

    private static void deleteUser(Admin admin) {
        System.out.print("Enter the ID of the user to delete: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Deleting user with ID: " + userId);
        
        boolean success = userService.deleteUser(userId);
        if (success) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("Failed to delete user.");
        }
    }


}






