package user;

import java.util.Arrays;

public enum UserType {
    ADMIN, EMPLOYEE, COMMON;

    public static UserType fromString(String userType) {
        if (userType == null) {
            throw new IllegalArgumentException("Null account type is not supported");
        }

        return Arrays.stream(values())
                .filter(type -> type.name().equals(userType.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid account type: " + userType));
    }
}
