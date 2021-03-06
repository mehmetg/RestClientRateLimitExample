import java.util.concurrent.TimeUnit;

/**
 * Created by mehmetgerceker on 5/2/16.
 */
public class RestExecutor {
    public static void main(String[] args){
        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");
        if (username == null || username.length() == 0 || accessKey == null || accessKey.length() == 0){
            System.err.println("Specify username and access token in env vars " +
                    "SAUCE_USERNAME and SAUCE_ACCESS_KEY please!");
            System.exit(1);
        }
        TestSLUsageAPI api = new TestSLUsageAPI();
        for(int i = 0; i < 1000; i++) {
            System.out.format("Executing request %d\n", i);
            try {
                System.out.println(String.format("%d: %s",
                        TimeUnit.NANOSECONDS.toSeconds(System.nanoTime()), api.getUsage(username, accessKey)));
                System.out.println("Success!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
