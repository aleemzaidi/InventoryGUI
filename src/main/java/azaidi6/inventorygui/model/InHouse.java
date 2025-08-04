package azaidi6.inventorygui.model;

/** Subclass of Part. This class inherits Part class */
public class InHouse extends Part {

    /** machine id of inHouse part */
    private int machineId;

    /** Constructor for InHouse class
     * @param id Part id
     * @param name Part name
     * @param price price of Part
     * @param stock inventory level of Part
     * @param min minimum quantity for Part
     * @param max maximum quantity for Part
     * @param machineId id of the InHouse Part
     * */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /** Setter for machineId
     * @param machineId sets the machineId for InHouse object
     * */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /** Getter for machineId
     * @return machineId
     * */
    public int getMachineId(){
        return machineId;
    }

}
