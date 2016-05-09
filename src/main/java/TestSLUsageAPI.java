import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.sun.xml.internal.ws.client.BindingProviderProperties;
import org.apache.commons.codec.binary.Base64;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestSLUsageAPI {


    public final int TOO_MANY_REQUESTS = 429;

    public String getUsage(String userName, String accesskey) throws Exception {
        String sauceRestActivityUrl = "https://saucelabs.com/rest/v1/%s/activity";
        AtomicInteger retries = new AtomicInteger(5);
        // Create JAX-RS client Jersey
        Client sauceUsageAPIClient = ClientBuilder.newClient();
        sauceUsageAPIClient.property(BindingProviderProperties.CONNECT_TIMEOUT, 300000);
        sauceUsageAPIClient.property(BindingProviderProperties.REQUEST_TIMEOUT, 300000);

        sauceRestActivityUrl = String.format(sauceRestActivityUrl, userName);

        WebTarget webTarget;
        Response response = null;
        String responseBody = null;
           /*Encode the access credentials*/
        String auth = userName + ":" + accesskey;

        auth = "Basic " + Base64.encodeBase64String(auth.getBytes());

        Response.StatusType respCode;
           /*Doing the REST service invocation*/
        while (retries.getAndDecrement() > 0) {
                webTarget = sauceUsageAPIClient.target(sauceRestActivityUrl);
                // add Authorization header
                Invocation.Builder invocationBuilder = webTarget
                        .request()
                        .header("Authorization", auth)
                        //this enables the rate limit functions.
                        .header("X-RateLimit-Enable", true);
                // invoke REST GET call.
                response = invocationBuilder.get();
                respCode = response.getStatusInfo();
                responseBody = response.readEntity(String.class);
                // Parse response only if response code is 200
                if (respCode.getStatusCode() != Response.Status.OK.getStatusCode()) {
                    if (respCode.getStatusCode() == TOO_MANY_REQUESTS){
                        Integer timeToRetry = Integer.valueOf(response.getHeaderString("Retry-After"));
                        System.err.println("You seem to have overused this endpoint." + String.format(
                                "Please retry in %d seconds.", timeToRetry));
                        if (retries.getAndDecrement() == 0) {
                            break;
                        }
                        Thread.sleep(TimeUnit.SECONDS.toMillis(timeToRetry));
                    } else {
                        throw new Exception("Error in fetching account usage . Response status code is " + respCode);
                    }
                }
            response.close();
        }
        return responseBody;
    }

}
