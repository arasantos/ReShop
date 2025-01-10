package comp3350.reshop.data.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import comp3350.reshop.data.UserData;
import comp3350.reshop.objects.User;

public class UserHSQLDB implements UserData {
    private final String dbPath;

    public UserHSQLDB(final String dbPath) {
        this.dbPath = dbPath;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    @Nonnull
    private User fromResultSet(@Nonnull final ResultSet rs) throws SQLException {
        final String userName = rs.getString("userName");
        final String userPassword = rs.getString("password");
        final String firstName = rs.getString("firstName");
        final String lastName = rs.getString("lastName");
        final String userCity = rs.getString("city");

        return new User(userName,userPassword, firstName, lastName, userCity);
    }
    @Override
    public void insertUser(@Nonnull User currentUser){
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?)");
            st.setString(1, currentUser.getUsername());
            st.setString(2, currentUser.getPassword());
            st.setString(3, currentUser.getFirstName());
            st.setString(4, currentUser.getLastName());
            st.setString(5, currentUser.getCity());

            st.executeUpdate();
        } catch (final SQLException e) {
            throw new DataException(e);
        }
    }

    @Override
    public User getUser(String username){
        User thisUser = null;

        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM users WHERE userName = ?");
            st.setString(1, username);
            final ResultSet rs = st.executeQuery();
            while(rs.next()) {
                thisUser = fromResultSet(rs);
            }
            rs.close();
            st.close();

            return thisUser;
        } catch (final SQLException e)
        {
            throw new DataException(e);
        }
    }

    @Override
    public void updateUser(String username, @Nonnull User newUser){
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("UPDATE users SET password = ?, firstName = ?, lastName = ?, city = ? WHERE userName = ?");
            st.setString(1, newUser.getPassword());
            st.setString(2, newUser.getFirstName());
            st.setString(3, newUser.getLastName());
            st.setString(4, newUser.getCity());
            st.setString(5, username);

            st.executeUpdate();

        } catch (final SQLException e) {
            throw new DataException(e);
        }
    }
}
