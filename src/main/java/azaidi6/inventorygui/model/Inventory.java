package azaidi6.inventorygui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class is the inventory class for the inventory management system*/
public class Inventory {

    /** List that holds all Parts in inventory */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /** List that holds all Products in inventory */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** Adds new part to allParts ObservableList
     * @param newPart Part to add to inventory
     * */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /** Adds new product to allProducts ObservableList
     * @param newProduct Product to add to inventory
     * */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /** Finds Part in allParts ObservableList with partId. Returns null if no match found.
     * @return null or Part from allParts matching partId
     * @param partId int variable used to search and return Part from allParts
     * */
    public static Part lookupPart(int partId){
        for(Part p : allParts) {
            if(p.getId() == partId) {
                return p;
            }
        }

        return null;
    }

    /** Finds Product in allProducts ObservableList with productId. Returns null if no match found.
     * @return null or Product from allProducts matching productId
     * @param productId int variable used to search and return Product from allProducts
     * */
    public static Product lookupProduct(int productId){
        for(Product p : allProducts) {
            if(p.getId() == productId) {
                return p;
            }
        }

        return null;
    }

    /** Returns a new ObservableList from allParts with all Part objects matching partName
     * @return new ObservableList of all parts containing partName
     * @param partName String variable used to search and return all parts matching partName from allParts
     * */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> result = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                result.add(part);
            }
        }
        return result;
    }

    /** Returns a new ObservableList from allProducts with all Product objects matching productName
     * @return new ObservableList of all products containing productName
     * @param productName String variable used to search and return all products matching productName from allProducts
     * */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> result = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                result.add(product);
            }
        }
        return result;
    }

    /** Updates Part in allParts ObservableList at specified index
     * @param index int variable for selecting which part in list to update
     * @param selectedPart new Part that will replace Part at specified index
     * */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /** Updates Product in allProducts ObservableList at specified index
     * @param index int variable for selecting which product in list to update
     * @param newProduct new Product that will replace Product at specified index
     * */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /** Removes a Part from allParts ObservableList
     * @return true or false for successful removal from list
     * @param selectedPart Part to remove from list
     * */
    public static boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }

    /** Removes a Product from allProducts ObservableList
     * @return true or false for successful removal from list
     * @param selectedProduct Product to remove from list
     * */
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }

    /** Returns all Part objects in allParts ObservableList
     * @return all Part objects in allParts ObservableList
     * */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    public static void setAllParts(ObservableList<Part> parts) {
        allParts = parts;
    }

    /** Returns all Product objects in allProducts ObservableList
     * @return all Product objects in allProducts ObservableList
     * */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    public static void setAllProducts(ObservableList<Product> products) {
        allProducts = products;
    }

    public static int findPartIndex(int searchId) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == searchId) {
                return i;  // Return the found index
            }
        }
        return -1;  // Return -1 if not found
    }

    public static int findProductIndex(int searchId) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == searchId) {
                return i;  // Return the found index
            }
        }
        return -1;  // Return -1 if not found
    }

}
