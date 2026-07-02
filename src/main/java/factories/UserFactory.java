package factories;

import models.request.RegistrationRequest;
import models.testdata.UserTestData;
import net.datafaker.Faker;

/*
    Factory Pattern
*/
public final class UserFactory {

    private static final Faker FAKER = new Faker();

    private UserFactory(){};

    public static UserTestData createUniqueUser(UserTestData user){

        // Generate runtime data (unique data)
        user.setEmail(generateUniqueEmail(user));
        user.setPhoneNumber(generateUniquePhone());

        return user;
    }

    public static String generateUniqueFirstName(UserTestData user){
        return String.format("%s.%d", user.getFirstName(), System.currentTimeMillis());
    }

    public static String generateUniqueLastName(UserTestData user){
        return String.format("%s.%d", user.getFirstName(), System.currentTimeMillis());
    }

    public static String generateUniqueEmail(UserTestData user){
        return String.format("%s.%s%d@gmail.com", user.getFirstName(), user.getLastName(), System.currentTimeMillis());
    }

    public static String generateUniquePhone(){
        return "780" + FAKER.number().digits(7);
    }

}
