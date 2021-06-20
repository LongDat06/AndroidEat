package Model;

public class Commen {
    public static User currentUser;
    public static  String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On my wad";
        else
            return "Shipped";
    }

    public static final String INTENT_FOOD_ID ="FoodId";
}
