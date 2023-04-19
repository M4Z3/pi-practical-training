package de.ma.pi.automation;

public class Main {
    public static void main(String[] args) {
        APIConnector apiConnector = new APIConnector("http://localhost:8080/engine-rest");

        System.out.println(apiConnector.start());
        System.out.println(apiConnector.getActive());
        System.out.println(apiConnector.getPendingUsertaskCallPresentOffer());
        System.out.println(apiConnector.completeUsertaskCallPresentOffer("e688dc88-de8c-11ed-a8a3-6c6a773c918d"));
        System.out.println(apiConnector.getPendingUsertaskCallAlternativeParts());
        System.out.println(apiConnector.getPendingUsertaskCheckPayment());
    }
}
