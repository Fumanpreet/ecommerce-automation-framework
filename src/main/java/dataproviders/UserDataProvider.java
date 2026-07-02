package dataproviders;

import models.testdata.UserTestData;
import org.testng.annotations.DataProvider;
import utils.JsonUtils;

import java.util.List;

public class UserDataProvider {

    @DataProvider(name = "successUserData")
    public static Object[][] getSuccessUserData() throws Exception {

        List<UserTestData> data =
                JsonUtils.readJsonList("testdata/user/userSuccess.json", UserTestData.class);

        return data.stream()
                .map(d -> new Object[]{d})
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "failureUserData")
    public static Object[][] getFailureUserData() throws Exception {

        List<UserTestData> data =
                JsonUtils.readJsonList("testdata/user/userFailure.json", UserTestData.class);

        return data.stream()
                .map(d -> new Object[]{d})
                .toArray(Object[][]::new);
    }
}
