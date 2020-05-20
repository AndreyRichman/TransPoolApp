package transpool.ui.console;

import transpool.ui.interfaces.UIHandler;
import javafx.util.Pair;
import transpool.ui.interfaces.UserRequest;
import transpool.ui.request.type.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
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
        boolean requestIsValid;
        do {
            showOptions();
            String chosenAction = getInput();
            requestIsValid = checkRequestIsValid(chosenAction);
            if (requestIsValid) {
                request = getRelevantRequest(chosenAction);
            }
            else{
                showOutput("Invalid input. please choose number ( 1 - 7 )");
            }
        }
        while (!requestIsValid);

        return request;
    }

    @Override
    public void showOptions() {
        String delimiter = String.join("", Collections.nCopies(10, " "));
        String question = String.format("%s%s%s",
                delimiter,
                "Select Desired Option:",
                delimiter);

        showTitle(question);
        stringNumToRequest .forEach((k,v) -> System.out.println(v.getKey()));
    }

    @Override
    public void showTitle(String title){
        int lengthOfRow = Math.max(title.length() + 2, 46);
        int numOfSpaces = (lengthOfRow - title.length()) / 2; //lengthOfRow == title.length() + 2? 1: lengthOfRow / 3;

        String spacesBeforeAndAfterTitle = String.join("", Collections.nCopies(numOfSpaces, " "));
        String signesAroundTitle = " " +  String.join("", Collections.nCopies(lengthOfRow, "-"));

        String titleToShow = String.join(System.lineSeparator(),
                signesAroundTitle,
                "|" + spacesBeforeAndAfterTitle + title + spacesBeforeAndAfterTitle + "|",
                signesAroundTitle
        );

        showOutput(titleToShow);
    }

    @Override
    public String getInput() {
        return this.inputReader.nextLine();
    }

    @Override
    public String getStringForQuestion(String question) {
        showQuestion(question);
        return getInput();
    }

    private void showQuestion(String question){
        System.out.print(question);
    }

    @Override
    public LocalTime getTimeFromUser(String question) {
        boolean inputIsValid = false;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);
        LocalTime inputTime = LocalTime.MIN;

        while (!inputIsValid){
            try {
                showQuestion(question);
                String input = getInput();
                inputTime = LocalTime.parse(input, timeFormatter);
                if (inputTime.getMinute() % 5 != 0)
                    showOutput("Invalid Minutes - must be multiply of 5(00, 05, 10...55)");
                else
                    inputIsValid = true;

            } catch (DateTimeParseException ignore){
                showOutput("Invalid time, use the following format: HH:mm (for example 17:00)");
            }
        }
        return inputTime;
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
        stringNumToRequest .put("2",(new Pair <> ("2. Create a new Ride ", NewRideRequest::new)));
        stringNumToRequest .put("3",(new Pair <> ("3. Create a new Tremp Request ", NewTrempRequest::new)));
        stringNumToRequest .put("4",(new Pair <> ("4. Show All Rides ", GetStatusOfRidesRequest::new)));
        stringNumToRequest .put("5",(new Pair <> ("5. Show All Tremp Requests ", GetStatusOfTrempsRequest::new)));
        stringNumToRequest .put("6",(new Pair <> ("6. Find a Match For Tremp Request ", TryMatchTrempToRideRequest::new)));
        stringNumToRequest .put("7",(new Pair <> ("7. Exit", ExitRequest::new)));

        return stringNumToRequest ;
    }

    private UserRequest getRelevantRequest(String chosenRequest)
    {
        return stringNumToRequest .get(chosenRequest).getValue().get();
    }

    private UserRequest getLoadXMLRequest(){
       return new LoadXMLRequest();
    }


    @Override
    public int showOptionsAndGetUserSelection(String titleForOptions, List<String> options){
        String delim = "-";
        System.out.println(String.join(System.lineSeparator(),
                String.join("", Collections.nCopies(titleForOptions.length(), delim)),
                titleForOptions,
                String.join("", Collections.nCopies(titleForOptions.length(), delim))
                )
        );
        int optionNumber = 1;
        for (String option : options) {
            showOutput(String.format("%d)  %s", optionNumber++, option));
        }
        System.out.println(String.join("", Collections.nCopies(titleForOptions.length(), delim)));
        int selectedNumber = getIndexFrom1To(options.size());

        return selectedNumber != - 1? selectedNumber - 1 : selectedNumber;
    }

    private int getIndexFrom1To(int maxIndex){
        boolean inputIsValid = false;
        int selectedIndex = 0;
        String inputLine;
        do {
            System.out.println(String.format("Please select desired option index (%d - %d), q for quit:", 1, maxIndex));
            inputLine = this.inputReader.nextLine();
            if(inputLine.equalsIgnoreCase("q"))
                return -1;
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
        boolean inputIsValid;

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
