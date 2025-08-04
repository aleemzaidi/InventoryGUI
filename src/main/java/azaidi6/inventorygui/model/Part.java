/**
 * Supplied class Part.java
 *
 * @author Aleem Zaidi
 **/

package azaidi6.inventorygui.model;

/** Abstract class. This is the parent class for InHouse and Outsourced */
public abstract class Part {

    /** Part id */
    private int id;

    /** Part name */
    private String name;

    /** Part price */
    private double price;

    /** Part inventory level */
    private int stock;

    /** Part minimum quantity */
    private int min;

    /** Part maximum quantity */
    private int max;

    /** Constructor for Part class
     * @param id Part id
     * @param name Part name
     * @param price price of Part
     * @param stock inventory level of Part
     * @param min minimum quantity of Part
     * @param max maximum quantity of Part
     * */
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /** Getter for part Id
     * @return part Id
     */
    public int getId() {
        return id;
    }

    /** Setter for part Id
     * @param id part Id
     */
    public void setId(int id) {
        this.id = id;
    }

    /** Getter for part name
     * @return name
     */
    public String getName() {
        return name;
    }

    /** Setter for part name
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Getter for part price
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /** Setter for part price
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /** Getter for part inventory level
     * @return stock
     */
    public int getStock() {
        return stock;
    }

    /** Setter for part inventory level
     * @param stock the inventory level of Part
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /** Getter for minimum inventory quantity
     * @return min
     */
    public int getMin() {
        return min;
    }

    /** Setter for minimum inventory quantity
     * @param min the minimum quantity
     */
    public void setMin(int min) {
        this.min = min;
    }

    /** Getter for maximum inventory quantity
     * @return max
     */
    public int getMax() {
        return max;
    }

    /** Setter for maximum inventory quantity
     * @param max the maximum quantity
     */
    public void setMax(int max) {
        this.max = max;
    }
    
}