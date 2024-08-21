package com.oz.awsjavasamples.employeeapi.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeRestRequestHandlerLambdaTest {

    @Mock
    private Context context;

    private EmployeeRestRequestHandlerLambda lambda;

    @Before
    public void setup() {
        lambda = new EmployeeRestRequestHandlerLambda();
        context = mock(Context.class);
    }

    @Test
    public void testGetAllEmployees() {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setHttpMethod("GET");
        requestEvent.setResource("/employees");

        APIGatewayProxyResponseEvent responseEvent = lambda.handleRequest(requestEvent, context);

        assertEquals(200, responseEvent.getStatusCode());
        assertEquals("[{\"id\": 1, \"name\": \"John Doe\", \"department\": \"Sales\"}, {\"id\": 2, \"name\": \"Jane Doe\", \"department\": \"Marketing\"}]", responseEvent.getBody());
    }

    @Test
    public void testCreateEmployee() {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setHttpMethod("POST");
        requestEvent.setResource("/employees");
        requestEvent.setBody("{\"name\": \"New Employee\", \"department\": \"Sales\"}");

        APIGatewayProxyResponseEvent responseEvent = lambda.handleRequest(requestEvent, context);

        assertEquals(200, responseEvent.getStatusCode());
        assertEquals("{\"id\": 3, \"name\": \"New Employee\", \"department\": \"Sales\"}", responseEvent.getBody());
    }

    @Test
    public void testGetEmployee() {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setHttpMethod("GET");
        requestEvent.setResource("/employees/1");

        APIGatewayProxyResponseEvent responseEvent = lambda.handleRequest(requestEvent, context);

        assertEquals(200, responseEvent.getStatusCode());
        assertEquals("{\"id\": 1, \"name\": \"John Doe\", \"department\": \"Sales\"}", responseEvent.getBody());
    }

    @Test
    public void testOptions() {
        APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setHttpMethod("OPTIONS");
        requestEvent.setResource("/employees");

        APIGatewayProxyResponseEvent responseEvent = lambda.handleRequest(requestEvent, context);

        assertEquals(200, responseEvent.getStatusCode());
        assertEquals("{}", responseEvent.getBody());
    }
}
