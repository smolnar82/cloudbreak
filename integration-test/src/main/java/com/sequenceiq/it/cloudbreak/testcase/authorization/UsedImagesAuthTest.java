package com.sequenceiq.it.cloudbreak.testcase.authorization;

import static com.sequenceiq.it.cloudbreak.context.RunningParameter.expectedMessage;

import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;

import org.testng.annotations.Test;

import com.sequenceiq.it.cloudbreak.client.UtilTestClient;
import com.sequenceiq.it.cloudbreak.context.Description;
import com.sequenceiq.it.cloudbreak.context.MockedTestContext;
import com.sequenceiq.it.cloudbreak.context.TestContext;
import com.sequenceiq.it.cloudbreak.dto.util.UsedImagesTestDto;
import com.sequenceiq.it.cloudbreak.testcase.mock.AbstractMockTest;

public class UsedImagesAuthTest extends AbstractMockTest {

    @Inject
    private UtilTestClient utilTestClient;

    @Override
    protected void setupTest(TestContext testContext) {
    }

    @Test(dataProvider = TEST_CONTEXT_WITH_MOCK)
    @Description(
            given = "there is a running cloudbreak",
            when = "a user without cloudbreakadmin/collectUsedImages right tries to call the used images endpoint",
            then = "it should return a forbidden response"
    )
    public void testUsedImagesWithZeroRights(MockedTestContext testContext) {
        useRealUmsUser(testContext, AuthUserKeys.ZERO_RIGHTS);

        testContext
                .given(UsedImagesTestDto.class)
                .whenException(utilTestClient.usedImages(), ForbiddenException.class, expectedMessage("You have no right to perform " +
                        "cloudbreakadmin/collectUsedImages in account 460c0d8f-ae8e-4dce-9cd7-2351762eb9ac"))
                .validate();
    }

    @Test(dataProvider = TEST_CONTEXT_WITH_MOCK)
    @Description(
            given = "there is a running cloudbreak",
            when = "a user with cloudbreakadmin/collectUsedImages right tries to call the used images endpoint",
            then = "it should return a valid response"
    )
    public void testUsedImagesWithSufficientRights(MockedTestContext testContext) {
        useRealUmsUser(testContext, AuthUserKeys.ACCOUNT_ADMIN);

        testContext
                .given(UsedImagesTestDto.class)
                .when(utilTestClient.usedImages())
                .validate();
    }
}
