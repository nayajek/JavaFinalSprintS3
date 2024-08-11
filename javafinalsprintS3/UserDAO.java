package src;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
    private Connection connection;

    public UserDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();  // Get the DB connection
    }

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());  // Assume password is already hashed
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getRole());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public User findUserByUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);  // Assume password is already hashed
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("buyer")) {
                    return new Buyer(resultSet.getString("username"),
                            resultSet.getString("password"), resultSet.getString("email"));
                } else if (role.equals("seller")) {
                    return new Seller(resultSet.getString("username"),
                            resultSet.getString("password"), resultSet.getString("email"));
                } else if (role.equals("admin")) {
                    return new Admin(resultSet.getString("username"),
                            resultSet.getString("password"), resultSet.getString("email"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    String query = "SELECT * FROM users";
    
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String email = resultSet.getString("email");
            String role = resultSet.getString("role");

            User user;
            switch (role) {
                case "buyer":
                    user = new Buyer(id, username, password, email);
                    break;
                case "seller":
                    user = new Seller(id, username, password, email);
                    break;
                case "admin":
                    user = new Admin(id, username, password, email);
                    break;
                default:
                    user = null;
            }
            if (user != null) {
                users.add(user);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return users;
}

public boolean deleteUserById(int userId) throws SQLException {
    String query = "DELETE FROM users WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, userId);
        int rowsAffected = statement.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        System.out.println("Error deleting user: " + e.getMessage());
        e.printStackTrace();
        throw e;  // Re-throw the exception after logging
    }
}

public boolean addUser(User user) throws SQLException {
    String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());  // The password should already be hashed
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getRole());

        int rowsAffected = statement.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public User authUser(String username, String password) throws SQLException {
        // SQL query to retrieve the user where username is given
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
                    // create return the User object If the password matches
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
}




