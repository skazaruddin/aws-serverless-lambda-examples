package com.oz.awsjavasamples.employeeapi.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class EmployeeRestRequestHandlerLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {

        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        try {

            String requestString = apiGatewayProxyRequestEvent.getBody();
            JSONParser parser = new JSONParser();
            JSONObject requestJsonObject = (JSONObject) parser.parse(requestString);
            String responseMessage = null;

            switch (apiGatewayProxyRequestEvent.getHttpMethod() + " " + apiGatewayProxyRequestEvent.getResource()) {
                case "GET /employees":
                    responseMessage = getAllEmployees();
                    break;
                case "POST /employees":
                    responseMessage = createEmployee(requestJsonObject);
                    break;
                case "GET /employees/{employeeId}":
                    String employeeId = apiGatewayProxyRequestEvent.getPathParameters().get("employeeId");
                    responseMessage = getEmployee(employeeId);
                    break;
                case "OPTIONS /employees":
                case "OPTIONS /employees/{employeeId}":
                    responseMessage = optionsResponse();
                    break;
                default:
                    responseMessage = "Invalid request";
            }

            generateResponse(apiGatewayProxyResponseEvent, responseMessage);

        } catch (ParseException e) {
            logger.log(e.fillInStackTrace().toString());
        }
        return apiGatewayProxyResponseEvent;
    }

    private String getAllEmployees() {
        // Return a list of all employees
        return "[{\"id\": 1, \"name\": \"John Doe\", \"department\": \"Sales\"}, {\"id\": 2, \"name\": \"Jane Doe\", \"department\": \"Marketing\"}]";
    }

    private String createEmployee(JSONObject requestJsonObject) {
        // Create a new employee
        String name = (String) requestJsonObject.get("name");
        String department = (String) requestJsonObject.get("department");
        return "{\"id\": 3, \"name\": \"" + name + "\", \"department\": \"" + department + "\"}";
    }

    private String getEmployee(String employeeId) {
        // Return the employee with the given ID
        return "{\"id\": " + employeeId + ", \"name\": \"John Doe\", \"department\": \"Sales\"}";
    }

    private String optionsResponse() {
        // Return an empty response with CORS headers
        return "{}";
    }

    private void generateResponse(APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent, String responseMessage) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Content-Type", "application/json");
        apiGatewayProxyResponseEvent.setHeaders(headers);
        apiGatewayProxyResponseEvent.setStatusCode(200);
        apiGatewayProxyResponseEvent.setBody(responseMessage);
    }
}

//    @Override
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
//
//        LambdaLogger logger = context.getLogger();
//        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
//        try {
//
//            String requestString = apiGatewayProxyRequestEvent.getBody();
//            JSONParser parser = new JSONParser();
//            JSONObject requestJsonObject = (JSONObject) parser.parse(requestString);
//            String requestMessage = null;
//            String responseMessage = null;
//            if (requestJsonObject != null) {
//                if (requestJsonObject.get("requestMessage") != null) {
//                    requestMessage = requestJsonObject.get("requestMessage").toString();
//                }
//            }
//            Map<String, String> responseBody = new HashMap<String, String>();
//            responseBody.put("responseMessage", "Hello World. Welcome to the first basic lambda.");
//            responseMessage = new JSONObject(responseBody).toJSONString();
//            generateResponse(apiGatewayProxyResponseEvent, responseMessage);
//
//        } catch (ParseException e) {
//            logger.log(e.fillInStackTrace().toString());
//        }
//        return apiGatewayProxyResponseEvent;
//    }
//
//    private void generateResponse(APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent, String requestMessage) {
//        apiGatewayProxyResponseEvent.setHeaders(Collections.singletonMap("timeStamp", String.valueOf(System.currentTimeMillis())));
//        apiGatewayProxyResponseEvent.setStatusCode(200);
//        apiGatewayProxyResponseEvent.setBody(requestMessage);
//    }

