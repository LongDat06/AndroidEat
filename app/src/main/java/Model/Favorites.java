package Model;

public class Favorites {
    private String FoodId;
    private String FoodName;
    private String FoodPrice;
    private String FoodMenu;
    private String FoodImg;
    private String UserPhone;

    public Favorites(String foodId, String foodName, String foodPrice, String foodMenu, String foodImg, String userPhone) {
        FoodId = foodId;
        FoodName = foodName;
        FoodPrice = foodPrice;
        FoodMenu = foodMenu;
        FoodImg = foodImg;
        UserPhone = userPhone;
    }

    public Favorites() {
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        FoodPrice = foodPrice;
    }

    public String getFoodMenu() {
        return FoodMenu;
    }

    public void setFoodMenu(String foodMenu) {
        FoodMenu = foodMenu;
    }

    public String getFoodImg() {
        return FoodImg;
    }

    public void setFoodImg(String foodImg) {
        FoodImg = foodImg;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
