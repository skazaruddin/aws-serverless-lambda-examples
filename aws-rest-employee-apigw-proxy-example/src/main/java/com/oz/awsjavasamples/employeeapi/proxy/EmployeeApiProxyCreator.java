package com.oz.awsjavasamples.employeeapi.proxy;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClient;
import com.amazonaws.services.apigateway.model.*;

public class EmployeeApiProxyCreator {
    public static void main(String[] args) {
        AmazonApiGateway apiGatewayClient = AmazonApiGatewayClient.builder().build();

// Create REST API
        CreateRestApiRequest createRestApiRequest = new CreateRestApiRequest()
                .withName("EmployeesAPI")
                .withDescription("API Gateway proxy for Employee Lambda function");

        CreateRestApiResult restApi = apiGatewayClient.createRestApi(createRestApiRequest);

// Create resource
        CreateResourceRequest createResourceRequest = new CreateResourceRequest();
        createResourceRequest.setRestApiId(restApi.getId());
        createResourceRequest.setParentId(restApi.getRootResourceId());
        createResourceRequest.setPathPart("employees");

        String resourceId = apiGatewayClient.createResource(createResourceRequest).getId();

// Create method
        CreateM createMethodRequest = new Create()
                .restApiId(restApi.id())
                .resourceId(resourceId)
                .httpMethod("POST")
                .authorization("NONE")
                .build();
        String methodId = apiGatewayClient.createMethod(createMethodRequest).id();

// Create integration
        Integration integration = new Integration();
        integration.setType("LAMBDA");
        integration.setUri("arn:aws:lambda:us-east-1:123456789012:function:BasicLambdaFunction");

        PutIntegrationRequest putIntegrationRequest = PutIntegrationRequest();
        putIntegrationRequest.setRestApiId(restApi.getId());
        putIntegrationRequest.setResourceId(resourceId);
        putIntegrationRequest.setHttpMethod("POST");
        putIntegrationRequest.setIntegrationHttpMethod("POST");
        putIntegrationRequest.set


        apiGatewayClient.putIntegration(putIntegrationRequest);

// Create method response
        MethodResponse methodResponse = MethodResponse.builder()
                .statusCode("200")
                .responseModels(Collections.singletonMap("application/json", "Empty"))
                .build();
        PutMethodRequest putMethodRequest = PutMethodRequest.builder()
                .restApiId(restApi.id())
                .resourceId(resourceId)
                .httpMethod("POST")
                .methodResponse(methodResponse)
                .build();
        apiGatewayClient.putMethod(putMethodRequest);
    }
}
