package comp3350.reshop.data.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import comp3350.reshop.logic.util.ClothingItemBuilder;
import comp3350.reshop.objects.ClothingItem;
import comp3350.reshop.data.ClothingItemData;
import comp3350.reshop.objects.NullClothingItem;

public class ClothingItemHSQLDB implements ClothingItemData{
    private final String dbPath;
    private final ClothingItemBuilder builder;

    public ClothingItemHSQLDB(final String dbPath, ClothingItemBuilder builder) {
        this.dbPath = dbPath;
        this.builder = builder;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    private ClothingItem fromResultSet(final ResultSet rs) throws SQLException {
        builder.setId(rs.getInt("itemID"));
        builder.setName(rs.getString("itemName"));
        builder.setDescription(rs.getString("description"));
        builder.setLocation(rs.getString("location"));
        builder.setType(rs.getString("type"));
        builder.setStyle(rs.getString("style"));
        builder.setQuality(rs.getString("quality"));
        builder.setPrice(rs.getInt("price"));
        builder.setImageUri(rs.getString("imageUri"));
        builder.setSeller(rs.getString("seller"));
        builder.setBuyer(rs.getString("buyer"));

        return builder.getProduct();
    }
    public void addItem(ClothingItem item) {
        try (final Connection c = connection()) {
            final PreparedStatement getMaxID = c.prepareStatement("SELECT max(ITEMID) as maxID from clothingItems");

            // set the item id as the current maximum id in the database + 1
            final ResultSet rs = getMaxID.executeQuery();
            int newID = 0;
            if (rs.next()) {
                newID = rs.getInt("maxID") + 1;
            }

            final PreparedStatement st = c.prepareStatement("INSERT INTO clothingItems VALUES(?,?,?,?,?,?,?,?, ?,?,? )");
            st.setInt(1, newID);
            st.setString(2, item.getItemName());
            st.setString(3, item.getDescription());
            st.setString(4,item.getLocation());
            st.setString(5,item.getType());
            st.setString(6,item.getStyle());
            st.setString(7,item.getQuality());
            st.setFloat(8,item.getPrice());
            st.setString(9,item.getImageUriString());
            st.setString(10,item.getSeller());
            st.setString(11,item.getBuyer());

            st.executeUpdate();

        } catch (final SQLException e) {
            throw new DataException(e);
        }
    }

    public ClothingItem getByItemID(int id) {
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM clothingItems WHERE itemID = ?");
            st.setInt(1, id);
            final ResultSet rs = st.executeQuery();

            if (rs.next()) {
                final ClothingItem item = fromResultSet(rs);
                rs.close();
                st.close();
                return item;
            }

            final ClothingItem notFound = new NullClothingItem();
            rs.close();
            st.close();
            return notFound;

        } catch (final SQLException e) {
            throw new DataException(e);
        }
    }

    public List<ClothingItem> getBySearchTerm(String searchTerm) {
        searchTerm = searchTerm.toLowerCase();
        final List<ClothingItem> clothingItemList = new ArrayList<>();
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM clothingItems WHERE (LOWER(itemName) LIKE ? OR LOWER(description) LIKE ?) AND buyer is NULL");
            st.setString(1, "%" + searchTerm + "%");
            st.setString(2, "%" + searchTerm + "%");

            final ResultSet rs = st.executeQuery();

            while (rs.next()) {
                final ClothingItem item = fromResultSet(rs);
                clothingItemList.add(item);
            }

            rs.close();
            st.close();

            return clothingItemList;
        } catch (final SQLException e) {
            throw new DataException(e);
        }
    }

    public List<ClothingItem> getBySeller(String name) {
        final List<ClothingItem> clothingItemList = new ArrayList<>();
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM clothingItems WHERE seller = ?");
            st.setString(1,name);

            final ResultSet rs = st.executeQuery();

            while (rs.next()) {
                final ClothingItem item = fromResultSet(rs);
                clothingItemList.add(item);
            }

            rs.close();
            st.close();

            return clothingItemList;
        } catch (final SQLException e) {
            throw new DataException(e);
        }
    }

    public List<ClothingItem> getByBuyer(String name) {
        final List<ClothingItem> clothingItemList = new ArrayList<>();
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM clothingItems WHERE buyer = ?");
            st.setString(1,name);

            final ResultSet rs = st.executeQuery();

            while (rs.next()) {
                final ClothingItem item = fromResultSet(rs);
                clothingItemList.add(item);
            }

            rs.close();
            st.close();

            return clothingItemList;
        } catch (final SQLException e) {
            throw new DataException(e);
        }
    }

    public List<ClothingItem> getItemsWithoutBuyer() {
        final List<ClothingItem> clothingItemList = new ArrayList<>();
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM clothingItems WHERE buyer is NULL");

            final ResultSet rs = st.executeQuery();

            while (rs.next()) {
                final ClothingItem item = fromResultSet(rs);
                clothingItemList.add(item);
            }

            rs.close();
            st.close();

            return clothingItemList;
        } catch (final SQLException e) {
            throw new DataException(e);
        }
    }

    public void setBuyer(String buyerName, int itemID){
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("UPDATE clothingItems SET buyer = ? WHERE itemID = ?");
            st.setString(1,buyerName);
            st.setInt(2,itemID);
            st.executeUpdate();

            st.close();
        } catch (final SQLException e) {
            throw new DataException(e);
        }
    }
}
