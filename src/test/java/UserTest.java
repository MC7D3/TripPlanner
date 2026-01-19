import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import tpgroup.model.domain.User;

class UserTest {
    
    @Test
    void testUserCreationWithPassword() {
        User user = new User("test@example.com", "password123");
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }
    
    @Test
    void testUserCreationWithoutPassword() {
        User user = new User("test@example.com");
        assertEquals("test@example.com", user.getEmail());
        assertNull(user.getPassword());
    }
    
    @Test
    void testUserEquality() {
        User user1 = new User("test@example.com", "pass1");
        User user2 = new User("test@example.com", "pass2");
        assertEquals(user1, user2); // Equality based on email only
    }
    
    @Test
    void testUserHashCode() {
        User user1 = new User("test@example.com", "pass1");
        User user2 = new User("test@example.com", "pass2");
        assertEquals(user1.hashCode(), user2.hashCode());
    }
    
    @Test
    void testSetPassword() {
        User user = new User("test@example.com");
        assertNull(user.getPassword());
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }
}
