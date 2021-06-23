package Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import Model.Commen;
import Model.Favorites;
import Model.Order;

public class Database extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "OrderCartDb.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    public List<Order> getCarts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSeclect = {"ID","ProductName","ProductId","Quantity","Price","Img"};
        String sqlTable ="OrderDetail";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSeclect,null,null,null,null,null);
        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                result.add(new Order(c.getInt(c.getColumnIndex("ID")),
                                c.getString(c.getColumnIndex("ProductId")),
                                c.getString(c.getColumnIndex("ProductName")),
                                c.getString(c.getColumnIndex("Quantity")),
                                c.getString(c.getColumnIndex("Price")),
                                c.getString(c.getColumnIndex("Img")))

                );
            }while (c.moveToNext());
        }
        return result;
    }

    public void addtoCarts(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Img) VALUES ('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getImg());
       db.execSQL(query);
    }

    public void cleanCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

    public void updateCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity = '%s' WHERE ID = '%d' ",order.getQuantity(),order.getID());
        db.execSQL(query);
    }

    public void addToFavorites(Favorites food){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(" + "FoodId,Phone,FoodName,FoodMenuID,FoodImg,FoodPrice)" + "VALUES ('%s','%s','%s','%s','%s','%s');",
                food.getFoodId(),
                food.getUserPhone(),
                food.getFoodName(),
                food.getFoodMenu(),
                food.getFoodImg(),
                food.getFoodPrice());
        db.execSQL(query);
    }

    public void removeFromFavorites(String foodId,String phone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FoodId='%s' and Phone='%s';",foodId,phone);
        db.execSQL(query);
    }

    public boolean isFavorites(String foodId,String phone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE FoodId='%s' and Phone='%s';",foodId,phone);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public List<Favorites> getFavorites(String phone){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSeclect = {"Phone","FoodId","FoodName","FoodPrice","FoodMenuId","FoodImg"};
        String sqlTable ="Favorites";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSeclect,"Phone=?",new String[]{phone},null,null,null,null);
        final List<Favorites> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                result.add(new Favorites(c.getString(c.getColumnIndex("FoodId")),
                        c.getString(c.getColumnIndex("FoodName")),
                        c.getString(c.getColumnIndex("FoodPrice")),
                        c.getString(c.getColumnIndex("FoodMenuId")),
                        c.getString(c.getColumnIndex("FoodImg")),
                        c.getString(c.getColumnIndex("Phone"))


                ));
            }while (c.moveToNext());
        }
        return result;
    }


}
