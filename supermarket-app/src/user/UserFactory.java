package user;

import supermarket.Supermarket;

public class UserFactory {

    public static User createUser(String userType, String username, Supermarket supermarket) {
        return createUser(UserType.fromString(userType), username, supermarket);
    }

    public static User createUser(UserType userType, String username, Supermarket supermarket) {
        return switch (userType) {
            case ADMIN -> new AdminUser(username, supermarket);
            case COMMON -> new CommonUser(username, supermarket);
            case EMPLOYEE -> new EmployeeUser(username, supermarket);
            default -> throw new IllegalArgumentException("Invalid user type");
        };
    }
}
