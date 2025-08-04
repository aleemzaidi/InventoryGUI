package azaidi6.inventorygui.dao;

import azaidi6.inventorygui.helper.JDBC;
import azaidi6.inventorygui.helper.TimeConversion;
import azaidi6.inventorygui.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class InventoryDAO {

    public static ObservableList<Part> getAllParts() {

        ObservableList<Part> parts = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM parts AS p " +
                         "LEFT JOIN in_house AS i ON p.id = i.part_id " +
                         "LEFT JOIN outsourced AS o ON p.id = o.part_id";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("p.id");
                String name = rs.getString("p.name");
                double price = rs.getDouble("p.price");
                int stock = rs.getInt("p.stock");
                int min = rs.getInt("p.min");
                int max = rs.getInt("p.max");
                int machineId = rs.getInt("i.machine_id");

                if(!rs.wasNull()) {
                    parts.add(new InHouse(id, name, price, stock, min, max, machineId));

                } else {
                    String companyName = rs.getString("o.companyName");
                    parts.add(new Outsourced(id, name, price, stock, min, max, companyName));
                }

            }

            return parts;

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static void addPart(String name, double price, int stock, int min, int max, String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdatedDate, int machineId) {

        try {
            JDBC.openConnection();
            String partSQL = "INSERT INTO parts (name, price, stock, min, max, createdBy, createdDate, lastUpdatedBy, lastUpdatedDate) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String machineSQL = "INSERT INTO in_house (part_id, machine_id) VALUES (?, ?)";

            PreparedStatement ps = JDBC.connection.prepareStatement(partSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, stock);
            ps.setInt(4, min);
            ps.setInt(5, max);
            ps.setString(6, createdBy);
            ps.setObject(7, TimeConversion.convertToUTC(createdDate));
            ps.setString(8, lastUpdatedBy);
            ps.setObject(9, TimeConversion.convertToUTC(lastUpdatedDate));
            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            int partId = -1;
            if (rs.next()) {
                partId = rs.getInt(1);
            }

            Inventory.addPart(new InHouse(partId, name, price, stock, min, max, machineId));
            ps = JDBC.connection.prepareStatement(machineSQL);
            ps.setInt(1, partId);
            ps.setInt(2, machineId);
            ps.executeUpdate();
            JDBC.closeConnection();


        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static void addPart(String name, double price, int stock, int min, int max, String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdatedDate, String companyName) {

        try {
            JDBC.openConnection();
            String partSQL = "INSERT INTO parts (name, price, stock, min, max, createdBy, createdDate, lastUpdatedBy, lastUpdatedDate) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String outsourcedSQL = "INSERT INTO outsourced (companyName, part_id) VALUES (?, ?)";

            PreparedStatement ps = JDBC.connection.prepareStatement(partSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, stock);
            ps.setInt(4, min);
            ps.setInt(5, max);
            ps.setString(6, createdBy);
            ps.setObject(7, TimeConversion.convertToUTC(createdDate));
            ps.setString(8, lastUpdatedBy);
            ps.setObject(9, TimeConversion.convertToUTC(lastUpdatedDate));
            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            int partId = -1;
            if (rs.next()) {
                partId = rs.getInt(1);
            }

            Inventory.addPart(new Outsourced(partId, name, price, stock, min, max, companyName));
            ps = JDBC.connection.prepareStatement(outsourcedSQL);
            ps.setString(1, companyName);
            ps.setInt(2, partId);
            ps.executeUpdate();
            JDBC.closeConnection();


        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static void updatePart(int id, String name, double price, int stock, int min, int max, String lastUpdatedBy, int machineId) {

        LocalDateTime now = LocalDateTime.now();

        try {
            JDBC.openConnection();

            String machineSQL = "SELECT o.id, i.id FROM parts AS p LEFT JOIN in_house AS i ON p.id = i.part_id " +
                    "LEFT JOIN outsourced AS o ON p.id = o.part_id WHERE p.id = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(machineSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                //grab the in_house id
                int mId = rs.getInt("i.id");

                //check if the value is not null
                if(!rs.wasNull()) {

                    //if not null, update the data
                    String sql = "UPDATE parts AS p " +
                            "LEFT JOIN in_house AS i ON p.id = i.part_id SET " +
                            "p.name = ?, " +
                            "p.price = ?, " +
                            "p.stock = ?, " +
                            "p.min = ?, " +
                            "p.max = ?, " +
                            "p.lastUpdatedBy = ?, " +
                            "p.lastUpdatedDate = ?, " +
                            "i.machine_id = ? " +
                            "WHERE p.id = ? " +
                            "AND i.id = ?";
                    ps = JDBC.connection.prepareStatement(sql);
                    ps.setString(1, name);
                    ps.setDouble(2, price);
                    ps.setInt(3, stock);
                    ps.setInt(4,min);
                    ps.setInt(5, max);
                    ps.setString(6, lastUpdatedBy);
                    ps.setObject(7, TimeConversion.convertToUTC(now));
                    ps.setInt(8, machineId);
                    ps.setInt(9, id);
                    ps.setInt(10, mId);
                    ps.executeUpdate();

                    //if it is null, check for outsourced id
                } else {
                    int cId = rs.getInt("o.id");

                    //check if outsourced id is not null
                    if(!rs.wasNull()) {

                        //if it's not null then prepare to delete the data from in_house
                        String deleteSQL = "DELETE FROM outsourced WHERE id = ?";
                        ps = JDBC.connection.prepareStatement(deleteSQL);
                        ps.setInt(1, cId);
                        ps.executeUpdate();

                        //add a new in_house part linked to this part id
                        String addSQL = "INSERT INTO in_house (machine_id, part_id) VALUES (?, ?)";
                        ps = JDBC.connection.prepareStatement(addSQL);
                        ps.setInt(1, machineId);
                        ps.setInt(2, id);
                        ps.executeUpdate();

                        //update remaining data that was changed in part table
                        String updateSQL = "UPDATE parts AS p SET " +
                                "p.name = ?, " +
                                "p.price = ?, " +
                                "p.stock = ?, " +
                                "p.min = ?, " +
                                "p.max = ?, " +
                                "p.lastUpdatedBy = ?, " +
                                "p.lastUpdatedDate = ? " +
                                "WHERE p.id = ?";
                        ps = JDBC.connection.prepareStatement(updateSQL);
                        ps.setString(1, name);
                        ps.setDouble(2, price);
                        ps.setInt(3, stock);
                        ps.setInt(4,min);
                        ps.setInt(5, max);
                        ps.setString(6, lastUpdatedBy);
                        ps.setObject(7, TimeConversion.convertToUTC(now));
                        ps.setInt(8, id);
                        ps.executeUpdate();

                    }
                }

                Inventory.updatePart(Inventory.findPartIndex(id), new InHouse(id, name, price, stock, min, max, machineId));
            }

            JDBC.closeConnection();
        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }
    }

    public static void updatePart(int id, String name, double price, int stock, int min, int max, String lastUpdatedBy, String companyName) {

        LocalDateTime now = LocalDateTime.now();

        try {
            JDBC.openConnection();

            String machineSQL = "SELECT o.id, i.id FROM parts AS p LEFT JOIN in_house AS i ON p.id = i.part_id " +
                    "LEFT JOIN outsourced AS o ON p.id = o.part_id WHERE p.id = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(machineSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                //grab the outsourced id
                int cId = rs.getInt("o.id");

                //check if the value is not null
                if(!rs.wasNull()) {

                    //if not null, update the data
                    String sql = "UPDATE parts AS p " +
                            "LEFT JOIN outsourced AS o ON p.id = o.part_id SET " +
                            "p.name = ?, " +
                            "p.price = ?, " +
                            "p.stock = ?, " +
                            "p.min = ?, " +
                            "p.max = ?, " +
                            "p.lastUpdatedBy = ?, " +
                            "p.lastUpdatedDate = ?, " +
                            "o.companyName = ? " +
                            "WHERE p.id = ? " +
                            "AND o.id = ?";
                    ps = JDBC.connection.prepareStatement(sql);
                    ps.setString(1, name);
                    ps.setDouble(2, price);
                    ps.setInt(3, stock);
                    ps.setInt(4,min);
                    ps.setInt(5, max);
                    ps.setString(6, lastUpdatedBy);
                    ps.setObject(7, TimeConversion.convertToUTC(now));
                    ps.setString(8, companyName);
                    ps.setInt(9, id);
                    ps.setInt(10, cId);
                    ps.executeUpdate();

                //if it is null, check for in_house id
                } else {
                    int mId = rs.getInt("i.id");

                    //check if in_house id is not null
                    if(!rs.wasNull()) {

                        //if it's not null then prepare to delete the data from in_house
                        String deleteSQL = "DELETE FROM in_house WHERE id = ?";
                        ps = JDBC.connection.prepareStatement(deleteSQL);
                        ps.setInt(1, mId);
                        ps.executeUpdate();

                        //add a new outsourced part linked to this part id
                        String addSQL = "INSERT INTO outsourced (companyName, part_id) VALUES (?, ?)";
                        ps = JDBC.connection.prepareStatement(addSQL);
                        ps.setString(1, companyName);
                        ps.setInt(2, id);
                        ps.executeUpdate();

                        //update remaining data that was changed in part table
                        String updateSQL = "UPDATE parts AS p SET " +
                                "p.name = ?, " +
                                "p.price = ?, " +
                                "p.stock = ?, " +
                                "p.min = ?, " +
                                "p.max = ?, " +
                                "p.lastUpdatedBy = ?, " +
                                "p.lastUpdatedDate = ? " +
                                "WHERE p.id = ?";
                        ps = JDBC.connection.prepareStatement(updateSQL);
                        ps.setString(1, name);
                        ps.setDouble(2, price);
                        ps.setInt(3, stock);
                        ps.setInt(4,min);
                        ps.setInt(5, max);
                        ps.setString(6, lastUpdatedBy);
                        ps.setObject(7, TimeConversion.convertToUTC(now));
                        ps.setInt(8, id);
                        ps.executeUpdate();
                    }
                }

                Inventory.updatePart(Inventory.findPartIndex(id), new Outsourced(id, name, price, stock, min, max, companyName));
            }


            JDBC.closeConnection();
        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }
    }

    public static void deletePart(Part part) {

        try {
            JDBC.openConnection();

            System.out.println("delete part id: " + part.getId());

            String partSQL = "SELECT o.id, i.id FROM parts AS p LEFT JOIN in_house AS i ON p.id = i.part_id " +
                    "LEFT JOIN outsourced AS o ON p.id = o.part_id WHERE p.id = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(partSQL);
            ps.setInt(1, part.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                //grab the outsourced id
                int cId = rs.getInt("o.id");

                //check if the value is not null
                if(!rs.wasNull()) {

                    //if it's not null then prepare to delete the data from outsourced
                    String deleteSQL = "DELETE FROM outsourced WHERE id = ?";
                    ps = JDBC.connection.prepareStatement(deleteSQL);
                    ps.setInt(1, cId);
                    ps.executeUpdate();

                    //if it is null, check for in_house id
                } else {
                    int mId = rs.getInt("i.id");

                    //check if in_house id is not null
                    if(!rs.wasNull()) {

                        //if it's not null then prepare to delete the data from in_house
                        String deleteSQL = "DELETE FROM in_house WHERE id = ?";
                        ps = JDBC.connection.prepareStatement(deleteSQL);
                        ps.setInt(1, mId);
                        ps.executeUpdate();

                    }
                }
            }

            //delete associated parts from inventory table
            String sql = "SELECT * FROM inventory AS inv " +
                    "WHERE inv.part_id = ?";
            ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, part.getId());
            rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("id");
                String deleteSQL = "DELETE FROM inventory AS inv " +
                        "WHERE inv.id = ?";
                PreparedStatement prep = JDBC.connection.prepareStatement(deleteSQL);
                prep.setInt(1, id);
                prep.executeUpdate();
            }

            //delete the data from parts
            String deletePartSQL = "DELETE FROM parts WHERE id = ?";
            ps = JDBC.connection.prepareStatement(deletePartSQL);
            ps.setInt(1, part.getId());
            ps.executeUpdate();

            Inventory.deletePart(part);

            JDBC.closeConnection();
        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static ObservableList<Product> getAllProducts() {

        ObservableList<Product> products = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM products AS p";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("p.id");
                String name = rs.getString("p.name");
                double price = rs.getDouble("p.price");
                int stock = rs.getInt("p.stock");
                int min = rs.getInt("p.min");
                int max = rs.getInt("p.max");

                products.add(new Product(id, name, price, stock, min, max));
            }

            return products;

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static int addProduct(String name, double price, int stock, int min, int max, String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdatedDate) {

        int productId = -1;

        try {
            JDBC.openConnection();
            String productSQL = "INSERT INTO products (name, price, stock, min, max, createdBy, createdDate, lastUpdatedBy, lastUpdatedDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = JDBC.connection.prepareStatement(productSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, stock);
            ps.setInt(4, min);
            ps.setInt(5, max);
            ps.setString(6, createdBy);
            ps.setObject(7, TimeConversion.convertToUTC(createdDate));
            ps.setString(8, lastUpdatedBy);
            ps.setObject(9, TimeConversion.convertToUTC(lastUpdatedDate));
            ps.executeUpdate();
            var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                productId = rs.getInt(1);
            }
            JDBC.closeConnection();

            Inventory.addProduct(new Product(productId, name, price, stock, min, max));

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

        return productId;
    }

    public static void updateProduct(int id, String name, double price, int stock, int min, int max, String lastUpdatedBy, LocalDateTime lastUpdatedDate) {

        int productId = -1;

        try {
            JDBC.openConnection();
            String productSQL = "UPDATE products SET " +
                    "name = ?, " +
                    "price = ?, " +
                    "stock = ?, " +
                    "min = ?, " +
                    "max = ?, " +
                    "lastUpdatedBy = ?, " +
                    "lastUpdatedDate = ? " +
                    "WHERE id = ?";

            PreparedStatement ps = JDBC.connection.prepareStatement(productSQL);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, stock);
            ps.setInt(4, min);
            ps.setInt(5, max);
            ps.setString(6, lastUpdatedBy);
            ps.setObject(7, TimeConversion.convertToUTC(lastUpdatedDate));
            ps.setInt(8,id);
            ps.executeUpdate();
            JDBC.closeConnection();

            Inventory.updateProduct(Inventory.findProductIndex(id), new Product(id, name, price, stock, min, max));

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static void addAssociatedPart(int productId, Part part) {

        try {
            JDBC.openConnection();

            String sql = "INSERT INTO inventory (part_id, product_id) VALUES (?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, part.getId());
            ps.setInt(2, productId);
            ps.executeUpdate();

            Inventory.getAllProducts().get(Inventory.findProductIndex(productId)).addAssociatedPart(part);

            JDBC.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static ObservableList<Part> getAllAssociatedParts(int productId) {

        ObservableList<Part> parts = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM parts AS p " +
                    "LEFT JOIN in_house AS i ON p.id = i.part_id " +
                    "LEFT JOIN outsourced AS o ON p.id = o.part_id " +
                    "LEFT JOIN inventory AS inv ON p.id = inv.part_id " +
                    "WHERE inv.product_id = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("p.id");
                String name = rs.getString("p.name");
                double price = rs.getDouble("p.price");
                int stock = rs.getInt("p.stock");
                int min = rs.getInt("p.min");
                int max = rs.getInt("p.max");
                int machineId = rs.getInt("i.machine_id");

                if(!rs.wasNull()) {
                    parts.add(new InHouse(id, name, price, stock, min, max, machineId));

                } else {
                    String companyName = rs.getString("o.companyName");
                    parts.add(new Outsourced(id, name, price, stock, min, max, companyName));
                }

            }
            Inventory.getAllProducts().get(Inventory.findProductIndex(productId)).setAllAssociatedParts(parts);

            return parts;

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static void deleteAssociatedParts(int productId) {

        Inventory.getAllProducts().get(Inventory.findProductIndex(productId)).getAllAssociatedParts().removeAll();

        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM inventory AS inv " +
                    "WHERE inv.product_id = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("id");
                String deleteSQL = "DELETE FROM inventory AS inv " +
                        "WHERE inv.id = ?";
                PreparedStatement prep = JDBC.connection.prepareStatement(deleteSQL);
                prep.setInt(1, id);
                prep.executeUpdate();
            }

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static void deleteProduct(Product product) {

        deleteAssociatedParts(product.getId());

        try {
            JDBC.openConnection();
            String deleteSQL = "DELETE FROM products AS p " +
                    "WHERE p.id = ?";
            PreparedStatement prep = JDBC.connection.prepareStatement(deleteSQL);
            prep.setInt(1, product.getId());
            prep.executeUpdate();

            Inventory.deleteProduct(product);

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static ObservableList<Part> getMinParts() {

        ObservableList<Part> parts = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM parts AS p " +
                    "LEFT JOIN in_house AS i ON p.id = i.part_id " +
                    "LEFT JOIN outsourced AS o ON p.id = o.part_id " +
                    "WHERE p.stock = p.min";
            PreparedStatement prep = JDBC.connection.prepareStatement(sql);
            ResultSet rs = prep.executeQuery();

            while(rs.next()) {

                int id = rs.getInt("p.id");
                String name = rs.getString("p.name");
                double price = rs.getDouble("p.price");
                int stock = rs.getInt("p.stock");
                int min = rs.getInt("p.min");
                int max = rs.getInt("p.max");
                int machineId = rs.getInt("i.machine_id");

                if(!rs.wasNull()) {
                    parts.add(new InHouse(id, name, price, stock, min, max, machineId));

                } else {
                    String companyName = rs.getString("o.companyName");
                    parts.add(new Outsourced(id, name, price, stock, min, max, companyName));
                }

            }

            return parts;

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static ObservableList<Part> getMaxParts() {

        ObservableList<Part> parts = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM parts AS p " +
                    "LEFT JOIN in_house AS i ON p.id = i.part_id " +
                    "LEFT JOIN outsourced AS o ON p.id = o.part_id " +
                    "WHERE p.stock = p.max";
            PreparedStatement prep = JDBC.connection.prepareStatement(sql);
            ResultSet rs = prep.executeQuery();

            while(rs.next()) {

                int id = rs.getInt("p.id");
                String name = rs.getString("p.name");
                double price = rs.getDouble("p.price");
                int stock = rs.getInt("p.stock");
                int min = rs.getInt("p.min");
                int max = rs.getInt("p.max");
                int machineId = rs.getInt("i.machine_id");

                if(!rs.wasNull()) {
                    parts.add(new InHouse(id, name, price, stock, min, max, machineId));

                } else {
                    String companyName = rs.getString("o.companyName");
                    parts.add(new Outsourced(id, name, price, stock, min, max, companyName));
                }

            }

            return parts;

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static ObservableList<Product> getMinProducts() {

        ObservableList<Product> products = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM products AS p " +
                    "WHERE p.stock = p.min";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("p.id");
                String name = rs.getString("p.name");
                double price = rs.getDouble("p.price");
                int stock = rs.getInt("p.stock");
                int min = rs.getInt("p.min");
                int max = rs.getInt("p.max");

                products.add(new Product(id, name, price, stock, min, max));
            }

            return products;

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static ObservableList<Product> getMaxProducts() {

        ObservableList<Product> products = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM products AS p " +
                    "WHERE p.stock = p.max";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("p.id");
                String name = rs.getString("p.name");
                double price = rs.getDouble("p.price");
                int stock = rs.getInt("p.stock");
                int min = rs.getInt("p.min");
                int max = rs.getInt("p.max");

                products.add(new Product(id, name, price, stock, min, max));
            }

            return products;

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static ObservableList<Part> getPartsByUser(String user) {

        ObservableList<Part> parts = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM parts AS p " +
                    "LEFT JOIN in_house AS i ON p.id = i.part_id " +
                    "LEFT JOIN outsourced AS o ON p.id = o.part_id " +
                    "WHERE p.lastUpdatedBy = ?";
            PreparedStatement prep = JDBC.connection.prepareStatement(sql);
            prep.setString(1, user);
            ResultSet rs = prep.executeQuery();

            while(rs.next()) {

                int id = rs.getInt("p.id");
                String name = rs.getString("p.name");
                double price = rs.getDouble("p.price");
                int stock = rs.getInt("p.stock");
                int min = rs.getInt("p.min");
                int max = rs.getInt("p.max");
                int machineId = rs.getInt("i.machine_id");

                if(!rs.wasNull()) {
                    parts.add(new InHouse(id, name, price, stock, min, max, machineId));

                } else {
                    String companyName = rs.getString("o.companyName");
                    parts.add(new Outsourced(id, name, price, stock, min, max, companyName));
                }

            }

            return parts;

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static ObservableList<Product> getProductsByUser(String user) {

        ObservableList<Product> products = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String sql = "SELECT * FROM products AS p " +
                    "WHERE p.lastUpdatedBy = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("p.id");
                String name = rs.getString("p.name");
                double price = rs.getDouble("p.price");
                int stock = rs.getInt("p.stock");
                int min = rs.getInt("p.min");
                int max = rs.getInt("p.max");

                products.add(new Product(id, name, price, stock, min, max));
            }

            return products;

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }
}
