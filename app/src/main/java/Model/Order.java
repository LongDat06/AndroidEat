package Model;

public class Order {
    private int ID;
    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Img;


    public Order(String productId, String productName, String quantity, String price, String img) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Img = img;
    }

    public Order(int ID, String productId, String productName, String quantity, String price, String img) {
        this.ID = ID;
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Img = img;
    }

    public Order() {
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
