
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tpgroup.model.domain.Room;
import tpgroup.model.domain.User;

class RoomTest {
    private User admin;
    private User member;
    
    @BeforeEach
    void setUp() {
        admin = new User("admin@example.com", "password");
        member = new User("member1@example.com", "password");
    }
    
    @Test
    void testRoomCreation() {
        Room room = new Room("TestRoom", admin, "Italy", "Rome");
        assertEquals("TestRoom", room.getName());
        assertEquals(admin, room.getAdmin());
        assertTrue(room.getMembers().contains(admin)); 
        assertEquals("Italy", room.getTrip().getCountry());
        assertEquals("Rome", room.getTrip().getMainCity());
        assertNotNull(room.getCode());
    }
    
    @Test
    void testCodeFormat() {
        Room room = new Room("Test Room", admin, "Italy", "Rome");
        String code = room.getCode();
        assertTrue(code.matches("^[a-z0-9]+(?:-[a-z0-9]+)*+-\\d{1,5}$"));
        assertTrue(code.startsWith("test-room-"));
    }
    
    @Test
    void testAddMember() {
        Room room = new Room("TestRoom", admin, "Italy", "Rome");
        assertTrue(room.add(member));
        assertTrue(room.isMember(member));
        assertEquals(2, room.size());
    }
    
    @Test
    void testAddDuplicateMember() {
        Room room = new Room("TestRoom", admin, "Italy", "Rome");
        room.add(member);
        assertFalse(room.add(member));
    }
    
    @Test
    void testRemoveMember() {
        Room room = new Room("TestRoom", admin, "Italy", "Rome");
        room.add(member);
        assertTrue(room.remove(member));
        assertFalse(room.isMember(member));
    }
    
    @Test
    void testRoomEquality() {
        Room room1 = new Room("code123");
        Room room2 = new Room("code123");
        assertEquals(room1, room2);
    }
}
