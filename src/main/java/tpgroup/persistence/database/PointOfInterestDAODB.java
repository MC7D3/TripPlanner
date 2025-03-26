package tpgroup.persistence.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Rating;
import tpgroup.model.domain.Tag;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.model.exception.SQLConnInterruptedException;
import tpgroup.persistence.DAO;

public class PointOfInterestDAODB implements DAO<PointOfInterest> {
    private final Connection connection;
    private final Gson gson = new Gson();

    public PointOfInterestDAODB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PointOfInterest get(PointOfInterest poi) throws RecordNotFoundException {
       final String query = "SELECT * FROM pointofinterest_tbl WHERE name = ? AND location = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, poi.getName());
            stmt.setString(2, poi.getLocation());
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPOI(rs);
            } else {
                throw new RecordNotFoundException();
            }
        } catch (SQLException e) {
            throw new SQLConnInterruptedException(e);
        } finally {
            closeQuietly(rs);
            closeQuietly(stmt);
        }
    }

    @Override
    public List<PointOfInterest> getAll() {
        final String query = "SELECT * FROM pointofinterest_tbl";
        Statement stmt = null;
        ResultSet rs = null;
        List<PointOfInterest> pois = new ArrayList<>();
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                pois.add(mapResultSetToPOI(rs));
            }
            return pois;
        } catch (SQLException e) {
            throw new SQLConnInterruptedException(e);
        } finally {
            closeQuietly(rs);
            closeQuietly(stmt);
        }
    }

    @Override
    public void save(PointOfInterest poi) {
        final String query = "UPDATE pointofinterest_tbl SET description = ?, rating = ?, tags = ? "
                           + "WHERE name = ? AND location = ?";
        PreparedStatement stmt = null;
        
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, poi.getDescription());
            stmt.setString(2, poi.getRating().name());
            stmt.setString(3, gson.toJson(poi.getTags()));
            stmt.setString(4, poi.getName());
            stmt.setString(5, poi.getLocation());
            
            if (stmt.executeUpdate() == 0) {
                add(poi);
            }
        } catch (SQLException e) {
            throw new SQLConnInterruptedException(e);
        } finally {
            closeQuietly(stmt);
        }
    }

    @Override
    public boolean add(PointOfInterest poi) {
        final String query = "INSERT INTO pointofinterest_tbl (name, location, description, rating, tags) "
                           + "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, poi.getName());
            stmt.setString(2, poi.getLocation());
            stmt.setString(3, poi.getDescription());
            stmt.setString(4, poi.getRating().name());
            stmt.setString(5, gson.toJson(poi.getTags()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate entry
                return false;
            }
            throw new SQLConnInterruptedException(e);
        } finally {
            closeQuietly(stmt);
        }
    }

    @Override
    public void delete(PointOfInterest poi) throws RecordNotFoundException {
        final String query = "DELETE FROM pointofinterest_tbl WHERE name = ? AND location = ?";
        PreparedStatement stmt = null;
        
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, poi.getName());
            stmt.setString(2, poi.getLocation());
            
            if (stmt.executeUpdate() == 0) {
                throw new RecordNotFoundException();
            }
        } catch (SQLException e) {
            throw new SQLConnInterruptedException(e);
        } finally {
            closeQuietly(stmt);
        }
    }

    @Override
    public List<PointOfInterest> getFiltered(Predicate<PointOfInterest> filter) {
        return getAll().stream()
                       .filter(filter)
                       .toList();
    }

    private PointOfInterest mapResultSetToPOI(ResultSet rs) throws SQLException {
        return new PointOfInterest(
            rs.getString("name"),
            rs.getString("location"),
            rs.getString("description"),
            Rating.valueOf(rs.getString("rating")),
            gson.fromJson(rs.getString("tags"), new TypeToken<List<Tag>>(){}.getType())
        );
    }

    private void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                // Log or handle at the application level
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }
}
