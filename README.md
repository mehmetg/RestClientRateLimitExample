# Rest Client Rate Limit Example

This example demonstrates the use of response headers to determine the 
rate limiting period using Sauce Labs REST API.
Endpoint: ```https://saucelabs.com/rest/v1/{username}/activity```
Retry delay: 30 seconds.
Max Retry count: 120
Default attempts using RestExecutor: 10
For more info: [Sauce Labs Documentation](https://wiki.saucelabs.com/display/InfoDev/Rate+Limits+for+the+Sauce+Labs+REST+API)

## Setup:
```export SAUCE_USERNAME=<sauce_user_name>```

```export SAUCE_ACCESS_KEY=<sauce_access_key>```

## Run:
```mvn exec:java```

## Notes:
Requires Java8