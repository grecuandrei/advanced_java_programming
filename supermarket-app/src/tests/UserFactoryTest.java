package tests;

import org.junit.Test;
import static org.junit.Assert.*;
import user.*;

public class UserFactoryTest {

    @Test
    public void testCreateAdminUser() {
        User user = UserFactory.createUser("admin", "adminUser", null);
        assertTrue(user instanceof AdminUser);
    }

    @Test
    public void testCreateEmployeeUser() {
        User user = UserFactory.createUser("employee", "employeeUser", null);
        assertTrue(user instanceof EmployeeUser);
    }

    @Test
    public void testCreateCommonUser() {
        User user = UserFactory.createUser("common", "commonUser", null);
        assertTrue(user instanceof CommonUser);
    }
}
