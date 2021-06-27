package Model;

import java.util.Date;

public class DishViewed {
    private String FoodId;
    private String FoodName;
    private String FoodPrice;
    private String FoodMenu;
    private String FoodImg;
    private String UserPhone;
    private String Time;

    public DishViewed(String foodId, String foodName, String foodPrice, String foodMenu, String foodImg, String userPhone, String time) {
        FoodId = foodId;
        FoodName = foodName;
        FoodPrice = foodPrice;
        FoodMenu = foodMenu;
        FoodImg = foodImg;
        UserPhone = userPhone;
        Time = time;
    }

    public DishViewed() {
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

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
