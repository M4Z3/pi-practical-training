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
import java.util.Random;

public class APIConnector {
    private String baseUrl;
    public APIConnector(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static final String[] deviceTypes = new String[]{"available_pc", "out_of_stock_pc", "not_functional_pc"};

    public String start() {
        Random random = new Random();

        try {
            String body = "{\n" +
                    "  \"messageName\" : \"Message_start_personal_computer_sale\",\n" +
                    "  \"processVariables\" : {\n" +
                    "    \"device_type\" : {\"value\" : \"" + deviceTypes[random.nextInt(3)] + "\", \"type\": \"String\"}\n" +
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

    public List<ProcessInstance> getPendingUsertaskCallPresentOffer() {
        try {
            HttpResponse<String> response = httpGetRequest("/task?taskDefinitionKey=Usertask_call_present_offer");
            return new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), ProcessInstance[].class)));
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
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProcessInstance> getPendingUsertaskCallAlternativeParts() {
        try {
            HttpResponse<String> response = httpGetRequest("/task?taskDefinitionKey=Usertask_call_alternative_parts");
            return new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), ProcessInstance[].class)));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String completeUsertaskCallAlternativeParts(String id) {
        Random random = new Random();
        try {
            String body = "{\n" +
                    "  \"variables\": {\n" +
                    "    \"order_cancelled\": {\"value\":\"answer_no\",\"type\":\"String\"},\n" +
                    "    \"new_device_type\": {\"value\":\"" + deviceTypes[random.nextInt(3)] + "\",\"type\":\"String\"}\n" +
                    "  }\n" +
                    "}";

            HttpResponse<String> response = httpPostRequest(String.format("/task/%s/submit-form", id), body);
            return response.body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProcessInstance> getPendingUsertaskCheckPayment() {
        try {
            HttpResponse<String> response = httpGetRequest("/task?taskDefinitionKey=Usertask_check_payment");
            return new ArrayList<>(Arrays.asList(new Gson().fromJson(response.body(), ProcessInstance[].class)));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String completeUsertaskCheckPayment(String id) {
        try {
            String body = "{\n" +
                    "  \"variables\": {\n" +
                    "    \"customer_paid\": {\"value\":\"answer_yes\",\"type\":\"String\"}\n" +
                    "  }\n" +
                    "}";

            HttpResponse<String> response = httpPostRequest(String.format("/task/%s/submit-form", id), body);
            return response.body();
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
