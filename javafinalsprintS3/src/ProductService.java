package src;

import java.sql.SQLException;
import java.util.List;


public class ProductService {

    private ProductDAO productDAO;

    public ProductService() throws SQLException {
        this.productDAO = new ProductDAO();
    }

    // Adding a new product
    public boolean addProduct(Product product) {
        return productDAO.addProduct(product);
    }

    // Getting all products
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    // Getting products by seller ID
    public List<Product> getProductsBySellerId(int sellerId) {
        return productDAO.getProductsBySellerId(sellerId);
    }

    public List<Product> getProductsBySeller(int sellerId) throws SQLException {
        return productDAO.getProductsBySellerId(sellerId);
    }

    public Product getProductById(int id) throws SQLException {
        return productDAO.getProductById(id);
    }
    
    // Updating a product by ID
    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }

    // Deleting a product by ID
    public boolean deleteProduct(int id) {
        return productDAO.deleteProduct(id);
    }
}

