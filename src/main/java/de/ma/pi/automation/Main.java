package de.ma.pi.automation;

import java.util.List;

public class Main {

    public static final int simulations = 1000;

    public static void main(String[] args) {
        APIConnector apiConnector = new APIConnector("http://localhost:8080/engine-rest");

        for(int i = 0; i < simulations; i++) {
            apiConnector.start();

            List<ProcessInstance> listPendingUsertaskCallPresentOffer = apiConnector.getPendingUsertaskCallPresentOffer();

            for (ProcessInstance instance: listPendingUsertaskCallPresentOffer) {
                apiConnector.completeUsertaskCallPresentOffer(instance.getId());
            }

            List<ProcessInstance> listPendingUsertaskCallAlternativeParts = apiConnector.getPendingUsertaskCallAlternativeParts();

            for (ProcessInstance instance: listPendingUsertaskCallAlternativeParts) {
                apiConnector.completeUsertaskCallAlternativeParts(instance.getId());
            }

            List<ProcessInstance> listPendingUsertaskCheckPayment = apiConnector.getPendingUsertaskCheckPayment();

            for (ProcessInstance instance: listPendingUsertaskCheckPayment) {
                apiConnector.completeUsertaskCheckPayment(instance.getId());
            }
        }
    }
}
