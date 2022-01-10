import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import Util.CSVTools;

public class Main {
    /**
     * Date, Time, Location, Operator, Flight.., Route, Type, Registration, cn.In,
     * Aboard, Fatalities, Ground, Survivors, SurvivalRate, Summary, ClustID
     */
    public static String[] HEADERS;
    public static Scanner sc;
    public static String dataPath;
    public static List<String[]> DATA;
    public static String EX_NUMBER_INPUT = "('100 200' or '100')";
    public static String EX_FIELD_INPUT = "(Date Location Time...)";

    public static void main(String[] args) throws Exception {
	sc = new Scanner(System.in);

	println("\r\n" // <br>
		+ " █████╗ ███╗   ██╗ █████╗ ██╗  ██╗   ██╗███████╗███████╗██████╗ \r\n"
		+ "██╔══██╗████╗  ██║██╔══██╗██║  ╚██╗ ██╔╝╚══███╔╝██╔════╝██╔══██╗\r\n"
		+ "███████║██╔██╗ ██║███████║██║   ╚████╔╝   ███╔╝ █████╗  ██████╔╝\r\n"
		+ "██╔══██║██║╚██╗██║██╔══██║██║    ╚██╔╝   ███╔╝  ██╔══╝  ██╔══██╗\r\n"
		+ "██║  ██║██║ ╚████║██║  ██║███████╗██║   ███████╗███████╗██║  ██║\r\n"
		+ "╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝╚═╝   ╚══════╝╚══════╝╚═╝  ╚═╝\r\n"
		+ "███████████████████████████████████████████████████████████████╗\r\n"
		+ "╚══════════════════════════════════════════════════════════════╝");
	println("Dataset Visualizer [Version 1.0.0]\r\n"
		+ "Copyright (c) 2021 Some Corporation. All rights reserved.\n");
	start();
	sc.close();
    }

    public static void enterDS() throws IOException {
	print(">> Enter the dataset(Demo): ");
	dataPath = sc.nextLine(); // FIXME: DELETE prefix
	dataPath = "./res/" + ((dataPath.equals("Demo")) ? "crash_data.csv" : dataPath);
	File f = new File(dataPath);

	if (!f.exists() || f.isDirectory()) {
	    printerr("Entered file is either missing or directory!");
	    print(">> Retry(Y/N): ");
	    String res = sc.nextLine().toLowerCase();

	    if (res.contains("n")) {
		System.exit(0);
	    } else {
		enterDS();
	    }

	}
	DATA = CSVTools.read(dataPath);
    }

    // crash_data.csv
    public static void start() throws IOException {
	enterDS(); // get filename from user
	println("Success!");
	println("Welcome to the Menu!\n");
	main_options();
    }

    // crash_data.csv
    public static void main_options() throws IOException {
	println("Menu Options:");
	String[] options = new String[] { // <br>
		"Display", // <br>
		"Sort", // <br>
		"Columns", // <br>
		"Filter", // <br>
		"Reset", // <br>
		"Save", // <br>
		"Exit" // <br>
	};
	printOptions(options);

	loop: while (true) {
	    int n = promptChoice();
	    String choice = options[n - 1];

	    switch (choice) {
	    case "Display":
		display_options();
		break loop;
	    case "Sort":
		boolean asc = promptInput("Ascend(true/false):")[0].equals("true");
		CSVTools.sort(DATA, promptInput("Select a Field to Sort on " + EX_FIELD_INPUT)[0], asc);
		CSVTools.display(DATA);
		main_options();
		break loop;
	    case "Columns":
		CSVTools.displayRow(CSVTools.getHeaders(DATA));
		main_options();
		break loop;
	    case "Filter":
		filter_options(promptInput("Select Fields to Filter on " + EX_FIELD_INPUT));
		break loop;
	    case "Save":

		break loop;
	    case "Reset":
		start();
		break loop;
	    case "Exit":
		System.exit(0);
		break loop;
	    default:
		printerr("Please try again!");
	    }
	}

    }

    // crash_data.csv
    public static void display_options() throws IOException {
	println("Display Options:");
	String[] options = new String[] { // <br>
		"Select All", // <br>
		"Range", // <br>
		"Columns", // <br>
	};
	printOptions(options);
	String[] fields, range;
	int len, start, end;
	loop: while (true) {
	    int n = promptChoice();
	    String choice = options[n - 1];

	    switch (choice) {
	    case "Select All":
		CSVTools.display(DATA);
		System.out.println();
		main_options();
		break loop;
	    case "Range":
		range = promptInput("Enter range " + EX_NUMBER_INPUT);

		len = range.length;
		start = (len > 1) ? Integer.parseInt(range[0].trim()) : 0;
		end = (len > 1) ? Integer.parseInt(range[1].trim()) : Integer.parseInt(range[0].trim());

		DATA = CSVTools.selectByRange(DATA, start, end, true);
		CSVTools.display(DATA);

		main_options();
		break loop;
	    case "Columns":
		fields = promptInput("Enter fields " + EX_FIELD_INPUT);
		DATA = CSVTools.selectByField(DATA, true, fields);
		CSVTools.display(DATA);
		main_options();
		break loop;

	    default:
		printerr("Please try again!");
	    }
	}
    }

    // crash_data.csv

    // crash_data.csv
    public static void filter_options(String... fields) throws IOException {
	println("Filter Options:");
	String[] options = new String[] { // <br>
		"Starts With", // <br>
		"Ends With", // <br>
		"Contains", // <br>
		"Has Null", // <br>
		"Numeric" };
	printOptions(options);

	loop: while (true) {
	    int n = promptChoice();
	    String choice = options[n - 1];
	    String key = null;
	    switch (choice) {
	    case "Starts With":
		key = promptInput("Enter your string:")[0];
		DATA = CSVTools.filter_string(DATA, CSVTools.getStartsWithRegex(key), fields);
		break loop;
	    case "Ends With":
		key = promptInput("Enter your string:")[0];
		DATA = CSVTools.filter_string(DATA, CSVTools.getEndsWithRegex(key), fields);
		break loop;
	    case "Contains":
		key = promptInput("Enter your string:")[0];
		DATA = CSVTools.filter_string(DATA, CSVTools.getContainsRegex(key), fields);
		break loop;
	    case "Numeric":
		filter_numeric();
		break loop;
	    case "Date":
		filter_date();
		break loop;

	    case "Has Null":
		key = promptInput("Type 'Y' for only-nulls, 'N' for non-nulls:")[0];
		DATA = CSVTools.filterNulls(DATA, key.contains("Y"), fields);
		break loop;
	    default:
		printerr("Please try again!");
	    }
	}
	CSVTools.display(DATA);
	main_options();
    }

    public static void filter_date() {
	println("Filter Options:");
	String[] options = new String[] { // <br>
		"Year", // <br>
		"Month", // <br>
		"Day" };
	printOptions(options);

	loop: while (true) {
	    int n = promptChoice();
	    String choice = options[n - 1];
	    String key = null;
	    switch (choice) {
	    case "Year":
		break loop;
	    case "Month":
		break loop;
	    case "Day":
		break loop;
	    }
	}
    }

    public static void filter_numeric() {
	println("Filter Options:");
	String[] options = new String[] { // <br>
		"Greater Than", // <br>
		"Greater and Equals", // <br>
		"Less Than", // <br>
		"Less and Equals", // <br>
		"Between" };
	printOptions(options);

	loop: while (true) {
	    int n = promptChoice();
	    String choice = options[n - 1];
	    String key = null;
	    switch (choice) {
	    case "Starts With":

	    }
	}
    }

    /**************************** AUX METHODS ************************************/
    // crash_data.csv
    private static String[] promptInput(String message) {
	print("\n>> " + message + ": ");
	String in = sc.nextLine();
	try {

	    return in.split(" ");
	} catch (Exception e) {
	    println("Something went wrong!");
	    return promptInput(message);
	}
    }

    private static int promptChoice() {
	return promptChoice("Enter your choice");
    }

    private static int promptChoice(String message) {
	print("\n>> " + message + ": ");
	int res = 0;
	try {
	    res = Integer.parseInt(sc.nextLine());
	} catch (Exception e) {
	    println("Invalid Choice!");
	    return promptChoice();
	}

	return res;
    }

    private static void printOptions(String[] S) {
	for (int i = 1; i <= S.length; i++) {
	    println("\t" + i + ": " + S[i - 1]);
	}
    }

    private static void print(Object o) {
	System.out.print(o);
    }

    private static void println(Object o) {
	System.out.println(o);
    }

    private static void printerr(Object o) {
	System.out.println("## " + o);
    }

}
