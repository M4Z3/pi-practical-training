package de.ma.pi.automation;

import camundajar.impl.com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class APIConnector {
    private String baseUrl;
    public APIConnector(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String start() {
        try {
            String body = "{\n" +
                    "  \"messageName\" : \"Message_start_personal_computer_sale\",\n" +
                    "  \"processVariables\" : {\n" +
                    "    \"device_type\" : {\"value\" : \"available_pc\", \"type\": \"String\"}\n" +
                    "  }\n" +
                    "}";

            HttpResponse<String> response = httpPostRequest("/message", body);
            return response.body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProcessInstance> getActive() {
        try {
            HttpResponse<String> response = httpGetRequest("/process-instance?active=true");
            return new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), ProcessInstance[].class)));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPendingUsertaskCallPresentOffer() {
        try {
            HttpResponse<String> response = httpGetRequest("/task?taskDefinitionKey=Usertask_call_present_offer");
            return response.body();
            //return new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), ProcessInstance[].class)));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String completeUsertaskCallPresentOffer(String id) {
        try {
            String body = "{\n" +
                    "  \"variables\": {\n" +
                    "    \"order_accepted\": {\"value\":\"answer_yes\",\"type\":\"String\"},\n" +
                    "    \"offer_requested\": {\"value\":\"answer_yes\",\"type\":\"String\"}\n" +
                    "  }\n" +
                    "}";

            HttpResponse<String> response = httpPostRequest(String.format("/task/%s/submit-form", id), body);
            return response.body();
            //return new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), ProcessInstance[].class)));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPendingUsertaskCallAlternativeParts() {
        try {
            HttpResponse<String> response = httpGetRequest("/task?taskDefinitionKey=Usertask_call_alternative_parts");
            return response.body();
            //return new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), ProcessInstance[].class)));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPendingUsertaskCheckPayment() {
        try {
            HttpResponse<String> response = httpGetRequest("/task?taskDefinitionKey=Usertask_check_payment");
            return response.body();
            //return new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), ProcessInstance[].class)));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse<String> httpGetRequest(String endpoint) throws URISyntaxException, IOException, InterruptedException  {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + endpoint))
                .headers("Content-Type", "application/json")
                .GET()
                .build();

        return HttpClient.newBuilder()
                .build().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> httpPostRequest(String endpoint, String body) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl + endpoint))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return HttpClient.newBuilder()
                .build().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
