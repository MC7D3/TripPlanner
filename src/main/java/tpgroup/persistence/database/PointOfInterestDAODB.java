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
	private static final String GET_QUERY = "SELECT name, description, city, coordinates_latitude, coordinates_longitude, country, rating, tags FROM poi_tbl WHERE name = ? AND coordinates_latitude = ? AND coordinates_longitude = ?";
    private static final String GETALL_QUERY = "SELECT name, description, city, coordinates_latitude, coordinates_longitude, country, rating, tags FROM poi_tbl";
    private static final String UPDATE_QUERY = "UPDATE poi_tbl SET description = ?, country = ?, city = ?, rating = ?, tags = ? WHERE name = ? AND coordinates_latitude = ? AND coordinates_longitude = ?";
	private static final String ADD_QUERY = "INSERT INTO poi_tbl (name, description, country, city, coordinates_latitude, coordinates_longitude, rating, tags) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM poi_tbl WHERE name = ? AND coordinates_latitude = ? AND coordinates_longitude = ?";

    public PointOfInterestDAODB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PointOfInterest get(PointOfInterest poi) throws RecordNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = connection.prepareStatement(GET_QUERY);
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
            close(rs);
            close(stmt);
        }
    }

    @Override
    public List<PointOfInterest> getAll() {
		Statement stmt = null;
        ResultSet rs = null;
        List<PointOfInterest> pois = new ArrayList<>();
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(GETALL_QUERY);
            
            while (rs.next()) {
                pois.add(mapResultSetToPOI(rs));
            }
            return pois;
        } catch (SQLException e) {
            throw new SQLConnInterruptedException(e);
        } finally {
            close(rs);
            close(stmt);
        }
    }

    @Override
    public void save(PointOfInterest poi) {
        PreparedStatement stmt = null;
        
        try {
            stmt = connection.prepareStatement(UPDATE_QUERY);
            stmt.setString(1, poi.getDescription());
            stmt.setString(2, poi.getCountry());
            stmt.setString(3, poi.getCity());
            stmt.setString(4, poi.getRating().name());
            stmt.setString(5, gson.toJson(poi.getTags()));
            stmt.setString(6, poi.getName());
            stmt.setDouble(7, poi.getCoordinates().getLatitude());
            stmt.setDouble(8, poi.getCoordinates().getLongitude());
            
            if (stmt.executeUpdate() == 0) {
                add(poi);
            }
        } catch (SQLException e) {
            throw new SQLConnInterruptedException(e);
        } finally {
            close(stmt);
        }
    }

    @Override
    public boolean add(PointOfInterest poi) {
        PreparedStatement stmt = null;
        
        try {
            stmt = connection.prepareStatement(ADD_QUERY);
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
            if (e.getErrorCode() == 1062) {
                return false;
            }
            throw new SQLConnInterruptedException(e);
        } finally {
            close(stmt);
        }
    }

    @Override
    public void delete(PointOfInterest poi) throws RecordNotFoundException {
        PreparedStatement stmt = null;
        
        try {
            stmt = connection.prepareStatement(DELETE_QUERY);
            stmt.setString(1, poi.getName());
            stmt.setDouble(2, poi.getCoordinates().getLatitude());
            stmt.setDouble(3, poi.getCoordinates().getLongitude());
            
            if (stmt.executeUpdate() == 0) {
                throw new RecordNotFoundException();
            }
        } catch (SQLException e) {
            throw new SQLConnInterruptedException(e);
        } finally {
            close(stmt);
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
			rs.getString("city"),
			rs.getString("country"),
			new Coordinates(rs.getDouble("coordinates_latitude"), rs.getDouble("coordinates_longitude")),
            Rating.valueOf(rs.getString("rating")),
            gson.fromJson(rs.getString("tags"), new TypeToken<List<Tag>>(){}.getType())
        );
    }

    private void close(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
				throw new IllegalStateException(e);
            }
        }
    }
}
