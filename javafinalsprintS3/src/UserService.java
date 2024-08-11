package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;


public class UserService {
    private UserDAO userDAO;

    public UserService() throws SQLException {
        this.userDAO = new UserDAO();  // Initialize UserDAO
        
    }

    public boolean registerUser(User user) throws SQLException {
        if (user == null) {
            System.out.println("User is null");
            return false;
        }
    
        // Hashing the user's password
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
    
        // Creating a new User object with the hashed password
        User newUser = new User(user.getId(), user.getUsername(), hashedPassword, user.getEmail(), user.getRole());
    
        // Adding the user to the database
        boolean isAdded = userDAO.addUser(newUser);
        
        if (isAdded) {
            System.out.println("User created successfully");
            return true;
        } else {
            System.out.println("Failed to create user");
            return false;
        }
    }
    
    public User authUser(String username, String password) throws SQLException {
    // SQL query to retrieve the user with the given username
    String query = "SELECT * FROM users WHERE username = ?";
    
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
        
        // Setting the username parameter in the SQL query
        statement.setString(1, username);
        
        // Executing the query
        ResultSet resultSet = statement.executeQuery();
        
        // Checking if a user with the given username was found
        if (resultSet.next()) {
            // Retrieving the stored hashed password from the database
            String storedHashedPassword = resultSet.getString("password");
            
            // Verifying the provided password against the stored hashed password
            if (BCrypt.checkpw(password, storedHashedPassword)) {
                // If the password matches, create and return the User object
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");
                
                return new User(id, username, storedHashedPassword, email, role);
            } else {
                // return null If the password does not match
                System.out.println("Authentication failed: Invalid password.");
                return null;
            }
        } else {
            // return null If no user with the given username was found
            System.out.println("Authentication failed: Username not found.");
            return null;
        }
    } catch (SQLException e) {
        System.out.println("Error during user authentication: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}


    public User login(String username, String password) {
        return userDAO.findUserByUsernameAndPassword(username, password);
    }

    public User loginUser(String username, String password) throws SQLException {
        return userDAO.authUser(username, password);
    }
    
    public List<User> getAllUsers() throws SQLException {
    return userDAO.getAllUsers();
}

    public boolean deleteUser(int userId) {
        try {
            return userDAO.deleteUserById(userId);
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}


