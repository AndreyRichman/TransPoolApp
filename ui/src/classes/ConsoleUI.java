package classes;

import interfaces.UIHandler;
import javafx.util.Pair;
import requests.classes.*;
import requests.interfaces.UserRequest;
import java.util.*;
import java.util.function.Supplier;


public class ConsoleUI implements UIHandler {

    private  Scanner inputReader;
    private Map<String,Pair <String, Supplier<UserRequest>>> stringNumToRequest ;

    public ConsoleUI() {
        this.inputReader = new Scanner(System.in);
        stringNumToRequest  = getEnumMap();
    }

    @Override
    public UserRequest getRequestFromUser(){
        UserRequest request = null;
        boolean requestIsValid = false;
        do {
            showOptions();
            String chosenAction = getInput();
            requestIsValid = checkRequestIsValid(chosenAction);
            if (requestIsValid) {
                request = getRelevantRequest(chosenAction);
            }
        }
        while (!requestIsValid);

        return request;
    }

    private void showErrorMessage(){
        //TODO: print some error message
    }

    @Override
    public void showOptions() {
        stringNumToRequest .forEach((k,v) -> System.out.println(v.getKey()));
    }

    @Override
    public String getInput() {
        return this.inputReader.nextLine();
    }

    @Override
    public void showOutput(String outputMsg) {
        System.out.println(outputMsg);
    }

    public void showErrorMsg(String outputMsg) {
        System.err.println(outputMsg);
    }

    private boolean checkRequestIsValid(String chosenRequest) {
        return stringNumToRequest .containsKey(chosenRequest);
    }

    private  Map<String,Pair <String, Supplier<UserRequest>>> getEnumMap(){

        Map<String,Pair <String, Supplier<UserRequest>>> stringNumToRequest  = new HashMap<>();

        stringNumToRequest .put("1",(new Pair <> ("1. Load XML File ",this::getLoadXMLRequest)));
        stringNumToRequest .put("2",(new Pair <> ("2. Ask for new tremp ", this::getRequestForNewTremp)));
        stringNumToRequest .put("3",(new Pair <> ("3. Show status of all avaible riders ", GetStatusOfRidesRequest::new)));
        stringNumToRequest .put("4",(new Pair <> ("4. Show status of all avaible temp requsts", GetStatusOfTrempsRequest::new)));
        stringNumToRequest .put("5",(new Pair <> ("5. Find a match ", TryMatchTrempToRideRequest::new)));
        stringNumToRequest .put("6",(new Pair <> ("6. Exit", ExitRequest::new)));

        return stringNumToRequest ;
    }

    private UserRequest getRelevantRequest(String chosenRequest)
    {
        return stringNumToRequest .get(chosenRequest).getValue().get();
    }

    private UserRequest getRequestForNewTremp() {
        NewTrempRequest req = new NewTrempRequest();

        showOutput("What your name?");
        req.setUserName(getInput());

        showOutput("what is your origen Station?");
        req.setFromStation(getInput());

        showOutput("What is your destanation Station?");
        req.setToStation(getInput());

        showOutput("departure time?");
        req.setDepartTime(getInput());

        showOutput("Direct only rides? Y/N");
        req.setDirectOnly((getInput().equalsIgnoreCase("Y")));

        return req;
    }

    private NewRideRequest getRequestForNewRide(){
        return new NewRideRequest();
    }

    private ExitRequest getExitRequest(){
        return new ExitRequest();
    }

    private UserRequest getMatchTrempToRideRequest(){
        TryMatchTrempToRideRequest newRequest = new TryMatchTrempToRideRequest();
        int trempID = 1;  //TODO: get from User
        int rideID = 400; //TODO: get from User
        newRequest.setTrempRequestID(trempID);
        newRequest.setRideID(rideID);

        return newRequest;
    }
    private UserRequest getStatusOfTrempsRequest(){
        return new GetStatusOfTrempsRequest();
    }

    private UserRequest getStatusOfRidesRequest(){
        return new GetStatusOfRidesRequest();
    }

    private UserRequest getLoadXMLRequest(){
       LoadXMLRequest req = new LoadXMLRequest();

       showOutput("Full file path");
       req.setFileDirectory(getInput());

        return req;
    }

    @Override
    public int showOptionsAndGetUserSelection(String titleForOptions, List<String> options){
        System.out.println(String.join(System.lineSeparator(),
                String.format("%s", '-' * 30),
                titleForOptions
                )
        );
        int optionNumber = 1;
        for (String option : options) {
            System.out.println(optionNumber++);
            System.out.println(option);
        }

        return getIndexFrom1To(options.size() + 1);
    }

    private int getIndexFrom1To(int maxIndex){
        boolean inputIsValid = false;
        int selectedIndex = 0;
        String inputLine;
        do {
            System.out.println(String.format("Please select desired option index (%d - %d):", 1, maxIndex));
            inputLine = this.inputReader.nextLine();
            try {
                selectedIndex = Integer.parseInt(inputLine);
                if (selectedIndex < 1 || selectedIndex > maxIndex)
                    throw new NumberFormatException();

                inputIsValid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number was entered.");
            }

        } while(!inputIsValid);

        return selectedIndex;
    }

    @Override
    public boolean getYesNoAnswerForQuestion(String question){
        System.out.println(question);
        return getYesOrNoFromUser();
    }

    public boolean getYesOrNoFromUser(){
        String input;
        boolean inputIsValid = false;

        do {
            System.out.print("Enter y/yes or n/no:");
            input = this.inputReader.nextLine();
            inputIsValid = strIsBooleanValue(input);

            if (!inputIsValid){
                System.out.println("Invalid input, please try again..");
            }
        }while (!inputIsValid);

        return convertBoolStrToBooleanValue(input);
    }

    private boolean strIsBooleanValue(String str){
        return str.equalsIgnoreCase("yes")
                || str.equalsIgnoreCase("no")
                || str.equalsIgnoreCase("y")
                || str.equalsIgnoreCase("n");
    }

    private boolean convertBoolStrToBooleanValue(String str){
        return str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("y");
    }

    @Override
    public int getNumberForString(String question){
        boolean inputIsValid = false;
        int enteredNumber = -1;
        String inputLine;
        do {
            System.out.println(question);
            inputLine = this.inputReader.nextLine();
            try {
                enteredNumber = Integer.parseInt(inputLine);
                if (enteredNumber < 0)
                    throw new NumberFormatException();

                inputIsValid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number was entered.");
            }

        } while(!inputIsValid);

        return enteredNumber;
    }

}
