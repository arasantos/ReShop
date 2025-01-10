package comp3350.reshop.application;

import comp3350.reshop.data.UserData;
import comp3350.reshop.data.hsqldb.ClothingItemHSQLDB;
import comp3350.reshop.data.hsqldb.UserHSQLDB;
import comp3350.reshop.data.ClothingItemData;
import comp3350.reshop.logic.util.ClothingItemBuilder;

public class Service {

    private static UserData userData = null;
    private static ClothingItemData clothingItemData = null;

    public static synchronized UserData getUserData() {
        if (userData == null) {
            userData = new UserHSQLDB(Main.getDBPathName());
        }

        return userData;
    }

    public static synchronized ClothingItemData getClothingItemData() {
        if (clothingItemData == null) {
            clothingItemData = new ClothingItemHSQLDB(Main.getDBPathName(), new ClothingItemBuilder());
        }

        return clothingItemData;
    }
}
