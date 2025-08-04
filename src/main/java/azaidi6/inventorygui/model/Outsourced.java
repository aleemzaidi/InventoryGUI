package azaidi6.inventorygui.model;

/** Subclass of Part */
public class Outsourced extends Part {

    /** company name of outsourced part */
    private String companyName;

    /** Constructor for Outsourced class
     * @param id Part id
     * @param name Part name
     * @param price price of Part
     * @param stock inventory level of Part
     * @param min minimum quantity
     * @param max maximum quantity
     * @param companyName Name of supplier
     * */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** Setter for companyName
     * @param companyName name to set Outsourced Part to
     * */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /** Getter for companyName
     * @return companyName for Outsourced Part
     * */
    public String getCompanyName(){
        return companyName;
    }
}
