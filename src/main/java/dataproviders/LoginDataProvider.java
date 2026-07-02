package dataproviders;

import models.testdata.LoginTestData;
import org.testng.annotations.DataProvider;
import utils.JsonUtils;

import java.util.List;

public class LoginDataProvider {

    @DataProvider(name = "successLoginData")
    public static Object[][] getSuccessLoginData() throws Exception {

        List<LoginTestData> data =
                JsonUtils.readJsonList("testdata/auth/loginSuccess.json", LoginTestData.class);

        return data.stream()
                .map(d -> new Object[]{d})
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "failureLoginData")
    public static Object[][] getFailureLoginData() throws Exception {

        List<LoginTestData> data =
                JsonUtils.readJsonList("testdata/auth/loginFailure.json", LoginTestData.class);

        return data.stream()
                .map(d -> new Object[]{d})
                .toArray(Object[][]::new);
    }
}