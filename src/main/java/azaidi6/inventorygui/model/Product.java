package azaidi6.inventorygui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Class for products in inventory */
public class Product {

    /** List for holding associated Parts of Product */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** Product id */
    private int id;

    /** Product name */
    private String name;

    /** Product price */
    private double price;

    /** Product inventory level */
    private int stock;

    /** Product minimum quantity */
    private int min;

    /** Product maximum quantity */
    private int max;

    /** Constructor for Product class
     * @param id Product id
     * @param name Product name
     * @param price Product price
     * @param stock inventory level of Product
     * @param min minimum quantity of Product
     * @param max maximum quantity of Product
     * */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock){
        this.stock = stock;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min){
        this.min = min;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max){
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param part the part to add
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * @param selectedAssociatedPart the part to delete
     */
    public void deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return the list of parts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

    public void setAllAssociatedParts(ObservableList<Part> parts) {
        associatedParts = parts;
    }
}
