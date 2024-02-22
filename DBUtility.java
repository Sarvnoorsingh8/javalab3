import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBBeverageUtility {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "";
    private static final String PASSWORD = "";

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    static {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            throw e;
        }
    }

    public static void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
    }

    public static List<BeverageSale> getBeverageSalesData() throws SQLException {
        List<BeverageSale> beverageSales = new ArrayList<>();
        String query = "SELECT * FROM beverage_sales";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                BeverageSale sale = new BeverageSale();
                sale.setId(resultSet.getInt("id"));
                sale.setProductName(resultSet.getString("product_name"));
                sale.setCategory(resultSet.getString("category"));
                sale.setUnitsSold(resultSet.getInt("units_sold"));
                sale.setRevenue(resultSet.getBigDecimal("revenue"));
                beverageSales.add(sale);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving beverage sales data: " + e.getMessage());
            throw e;
        }
        return beverageSales;
    }

    public static void insertBeverageSale(BeverageSale sale) throws SQLException {
        String query = "INSERT INTO beverage_sales (product_name, category, units_sold, revenue) VALUES (?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, sale.getProductName());
            preparedStatement.setString(2, sale.getCategory());
            preparedStatement.setInt(3, sale.getUnitsSold());
            preparedStatement.setBigDecimal(4, sale.getRevenue());
            preparedStatement.executeUpdate();
            System.out.println("Sale data inserted successfully");
        } catch (SQLException e) {
            System.err.println("Error inserting beverage sale data: " + e.getMessage());
            throw e;
        }
    }
}
