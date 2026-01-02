package tpgroup.persistence.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import tpgroup.model.domain.User;
import tpgroup.model.exception.RecordNotFoundException;
import tpgroup.model.exception.SQLConnInterruptedException;
import tpgroup.persistence.DAO;

public class UserDAODB implements DAO<User> {
	Connection conn;

	public UserDAODB(Connection conn) {
		this.conn = conn;
	}

	@Override
	public User get(User user) throws RecordNotFoundException {
		final String query = "SELECT email, password FROM user_tbl WHERE email = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, user.getEmail());
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new User(rs.getString("email"), rs.getString("password"));
			} else {
				throw new RecordNotFoundException();
			}
		} catch (SQLException e) {
			throw new SQLConnInterruptedException(e);
		}
	}

	@Override
	public void save(User user) {
		final String query = "UPDATE user_tbl SET password = ? WHERE email = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, user.getPassword());
			stmt.setString(2, user.getEmail());

			if (stmt.executeUpdate() == 0) {
				add(user);
			}

		} catch (SQLException e) {
			throw new SQLConnInterruptedException(e);
		}
	}

	@Override
	public List<User> getAll() {
		final String query = "SELECT email, password FROM user_tbl";
		List<User> users = new ArrayList<>();

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				users.add(new User(
						rs.getString("email"),
						rs.getString("password")));
			}
		} catch (SQLException e) {
			throw new SQLConnInterruptedException(e);
		}
		return users;
	}

	@Override
	public List<User> getFiltered(Predicate<User> filter) {
		return getAll().stream()
				.filter(filter)
				.toList();
	}

	@Override
	public boolean add(User user) {
		final String query = "INSERT INTO user_tbl (email, password) VALUES (?, ?)";
		boolean ret;

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getPassword());

			ret = stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) { // Duplicate entry error code
				return false;
			}
			throw new SQLConnInterruptedException(e);
		}

		return ret;
	}

	@Override
	public void delete(User user) throws RecordNotFoundException {
		final String query = "DELETE FROM user_tbl WHERE email = ?";

		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, user.getEmail());

			if (stmt.executeUpdate() == 0) {
				throw new RecordNotFoundException();
			}

		} catch (SQLException e) {
			throw new SQLConnInterruptedException(e);
		}
	}

}
