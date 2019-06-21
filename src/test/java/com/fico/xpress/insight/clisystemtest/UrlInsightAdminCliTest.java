package com.fico.xpress.insight.clisystemtest;

import org.junit.Test;

import java.io.IOException;

import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.INSIGHT_HOST;
import static com.fico.xpress.insight.clisystemtest.InsightCmdLineTestConstants.WINDOWS_COMMAND;
import static org.junit.Assert.assertTrue;

public class UrlInsightAdminCliTest {

    @Test
    public void verifyUrlValidationWithPassingUrl() throws IOException {
        String[] verifyUrlValidationCmd = (WINDOWS_COMMAND+" user list -u admin -p admin123 -url ").split(" ");
        String expectedResultWhenHostURlMissing = "Missing argument for option: url. Process finished with exit code 1";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyUrlValidationCmd).contains(expectedResultWhenHostURlMissing));
    }

    @Test
    public void verifyUrlValidationWithPassingHttpsUrl() throws IOException {

        String[] verifyUrlValidationCmd = (WINDOWS_COMMAND+" user list -u admin -p admin123 -url " + INSIGHT_HOST.replace("http", "https")).split(" ");
        String expectedResultWhenHostURlMissing = "Could not connect to server.";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyUrlValidationCmd).split("\n")[1].startsWith(expectedResultWhenHostURlMissing));
    }

    @Test
    public void verifyUrlNegativeValidationWithPassingHttpUrl() throws IOException {
        String[] verifyUrlValidationCmd = (WINDOWS_COMMAND+" user list -u admin -p admin123 -Url" + INSIGHT_HOST).split(" ");
        String expectedResultWhenHostURlAppended = "Unrecognized option";
        assertTrue(UtilsCLITest.consoleCommandExecuter(verifyUrlValidationCmd).startsWith(expectedResultWhenHostURlAppended));
    }
}
