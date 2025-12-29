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

import tpgroup.model.domain.Coordinates;
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
       final String query = "SELECT * FROM pointofinterest_tbl WHERE name = ? AND latitude = ? AND longitude = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, poi.getName());
            stmt.setDouble(2, poi.getCoordinates().getLatitude());
            stmt.setDouble(3, poi.getCoordinates().getLongitude());
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
        final String query = "UPDATE pointofinterest_tbl SET description = ?, country = ?, city = ?, rating = ?, tags = ? "
                           + "WHERE name = ? AND latitude = ? AND longitude = ?";
        PreparedStatement stmt = null;
        
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, poi.getDescription());
            stmt.setString(2, poi.getCountry());
            stmt.setString(2, poi.getCity());
            stmt.setString(3, poi.getRating().name());
            stmt.setString(4, gson.toJson(poi.getTags()));
            stmt.setString(5, poi.getName());
            stmt.setDouble(6, poi.getCoordinates().getLatitude());
            stmt.setDouble(6, poi.getCoordinates().getLongitude());
            
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
        final String query = "INSERT INTO pointofinterest_tbl (name, description, country, city, latitude, longitude, rating, tags) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, poi.getName());
            stmt.setString(2, poi.getDescription());
            stmt.setString(3, poi.getCountry());
            stmt.setString(4, poi.getCity());
            stmt.setDouble(5, poi.getCoordinates().getLatitude());
            stmt.setDouble(6, poi.getCoordinates().getLongitude());
            stmt.setString(7, poi.getRating().name());
            stmt.setString(8, gson.toJson(poi.getTags()));
            
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
        final String query = "DELETE FROM pointofinterest_tbl WHERE name = ? AND latitude = ? AND longitude = ?";
        PreparedStatement stmt = null;
        
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, poi.getName());
            stmt.setDouble(2, poi.getCoordinates().getLatitude());
            stmt.setDouble(3, poi.getCoordinates().getLongitude());
            
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
            rs.getString("description"),
			rs.getString("country"),
			rs.getString("city"),
			new Coordinates(rs.getDouble("latitude"), rs.getDouble("longitude")),
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
