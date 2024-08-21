package com.oz.awsjavasamples.employeeapi.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertEquals;

public class EmployeeRestRequestHandlerLambdaIntegrationTest {

    private EmployeeRestRequestHandlerLambda lambda;

    @Rule
    public WireMockClassRule wireMockRule = new WireMockClassRule(8089);

    @Before
    public void setup() {
        lambda = new EmployeeRestRequestHandlerLambda();
        wireMockRule.stubFor(get(urlEqualTo("/employees"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 1, \"name\": \"John Doe\", \"department\": \"Sales\"}, {\"id\": 2, \"name\": \"Jane Doe\", \"department\": \"Marketing\"}]")));
    }

    @Test
    public void testGetAllEmployees() {
        // Create a test event
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setHttpMethod("GET");
        requestEvent.setResource("/employees");

        // Call the lambda function
        APIGatewayProxyResponseEvent responseEvent = lambda.handleRequest(requestEvent, null);

        // Verify the response
        assertEquals(200, responseEvent.getStatusCode());
        assertEquals("[{\"id\": 1, \"name\": \"John Doe\", \"department\": \"Sales\"}, {\"id\": 2, \"name\": \"Jane Doe\", \"department\": \"Marketing\"}]", responseEvent.getBody());
    }

    @Test
    public void testCreateEmployee() {
        // Create a test event
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setHttpMethod("POST");
        requestEvent.setResource("/employees");
        requestEvent.setBody("{\"name\": \"New Employee\", \"department\": \"Sales\"}");

        // Call the lambda function
        APIGatewayProxyResponseEvent responseEvent = lambda.handleRequest(requestEvent, null);

        // Verify the response
        assertEquals(200, responseEvent.getStatusCode());
        assertEquals("{\"id\": 3, \"name\": \"New Employee\", \"department\": \"Sales\"}", responseEvent.getBody());
    }

    @Test
    public void testGetEmployee() {
        // Create a test event
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setHttpMethod("GET");
        requestEvent.setResource("/employees/1");

        // Call the lambda function
        APIGatewayProxyResponseEvent responseEvent = lambda.handleRequest(requestEvent, null);

        // Verify the response
        assertEquals(200, responseEvent.getStatusCode());
        assertEquals("{\"id\": 1, \"name\": \"John Doe\", \"department\": \"Sales\"}", responseEvent.getBody());
    }

    @Test
    public void testOptions() {
        // Create a test event
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setHttpMethod("OPTIONS");
        requestEvent.setResource("/employees");

        // Call the lambda function
        APIGatewayProxyResponseEvent responseEvent = lambda.handleRequest(requestEvent, null);

        // Verify the response
        assertEquals(200, responseEvent.getStatusCode());
        assertEquals("{}", responseEvent.getBody());
    }
}
