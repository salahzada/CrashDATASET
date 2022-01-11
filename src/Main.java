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

	println(//
		"\r\n" + " █████╗ ███╗   ██╗ █████╗ ██╗  ██╗   ██╗███████╗███████╗██████╗ \r\n"
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
	try {
	    main_options();
	} catch (Exception e) {
	    System.out.println("Something went wrong! Please type column names exactly.\n" + e.getMessage());
	    e.printStackTrace();
	    start();
	}
    }

    // crash_data.csv
    public static void main_options() throws IOException {
	println("Menu Options:");
	String[] options = new String[] { "Display", "Sort", "Search", "Columns", "Filter", "Reset", "Save", "Exit" };
	printOptions(options);

	loop: while (true) {
	    int n = promptChoice();
	    String choice = options[n - 1];

	    switch (choice) {
	    case "Display":
		display_options();

		break loop;

	    case "Search":
		String[] fields = promptInput("Enter fields " + EX_FIELD_INPUT + ":");
		String term = promptInput("Enter term to search:")[0];
		DATA = CSVTools.search(DATA, fields[0], term);

		break loop;
	    case "Sort":
		boolean asc = promptInput("Ascend(true/false):")[0].equals("true");
		CSVTools.sort(DATA, promptInput("Select a Field to Sort on " + EX_FIELD_INPUT)[0], asc);

		break loop;
	    case "Columns":
		CSVTools.displayRow(CSVTools.getHeaders(DATA));
		main_options();
		return;
	    case "Filter":
		filter_options();
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
	CSVTools.display(DATA);
	main_options();

    }

    // crash_data.csv
    public static void display_options() throws IOException {
	println("Display Options:");
	String[] options = new String[] { "Select All", "Range", "Columns", };
	printOptions(options);
	String[] fields, range;
	int len, start, end;
	loop: while (true) {
	    int n = promptChoice();
	    String choice = options[n - 1];

	    switch (choice) {
	    case "Select All":
		break loop;
	    case "Range":
		range = promptInput("Enter range " + EX_NUMBER_INPUT + ":");

		len = range.length;
		start = (len > 1) ? Integer.parseInt(range[0].trim()) : 0;
		end = (len > 1) ? Integer.parseInt(range[1].trim()) : Integer.parseInt(range[0].trim());

		DATA = CSVTools.selectByRange(DATA, start, end, true);

		break loop;
	    case "Columns":
		fields = promptInput("Enter fields " + EX_FIELD_INPUT + ":");
		DATA = CSVTools.selectByField(DATA, true, fields);

		break loop;

	    default:
		printerr("Please try again!");
	    }
	}
    }

    // crash_data.csv

    // crash_data.csv
    public static void filter_options() throws IOException {
	println("Filter Options:");
	String[] options = new String[] { "Starts With", "Ends With", "Contains", "Has Null", "Date", "Numeric",
		"Fatality" };
	printOptions(options);

	loop: while (true) {
	    int n = promptChoice();
	    String choice = options[n - 1];
	    String key = null;
	    String[] fields = null;
	    switch (choice) {
	    case "Starts With":
		fields = promptInput("Select Fields to Filter on " + EX_FIELD_INPUT + ":");
		key = promptInput("Enter your string:")[0];
		DATA = CSVTools.filter_string(DATA, CSVTools.getStartsWithRegex(key), fields);
		break loop;
	    case "Ends With":
		fields = promptInput("Select Fields to Filter on " + EX_FIELD_INPUT + ":");
		key = promptInput("Enter your string:")[0];
		DATA = CSVTools.filter_string(DATA, CSVTools.getEndsWithRegex(key), fields);
		break loop;
	    case "Contains":
		fields = promptInput("Select Fields to Filter on " + EX_FIELD_INPUT + ":");
		key = promptInput("Enter your string:")[0];
		DATA = CSVTools.filter_string(DATA, CSVTools.getContainsRegex(key.toLowerCase()), fields);
		break loop;
	    case "Numeric":
		fields = promptInput("Select Fields to Filter on " + EX_FIELD_INPUT + ":");
		filter_numeric(fields);
		break loop;
	    case "Date":
		filter_date();
		break loop;
	    case "Fatality":
		key = promptInput("High or Low:")[0];
		DATA = CSVTools.filter_string(DATA, CSVTools.getStartsWithRegex(key), true, "ClustID");
		break loop;
	    case "Has Null":
		key = promptInput("Type 'Y' for only-nulls, 'N' for non-nulls:")[0];
		DATA = CSVTools.filterNulls(DATA, key.contains("Y"), fields);
		break loop;
	    default:
		printerr("Please try again!");
	    }
	}
    }

    public static void filter_numeric(String... fields) {
	println("Filter Options:");
	String[] options = new String[] { "equal(Ex: eq 0)", "greater than(Ex: gt 0)", "less than(Ex: lt 0)",
		"greater and equal to(Ex: ge 0)", "less and equal to(Ex: le 0)", "between(Ex: bt 1 5)", "null(Ex: 0)" };

	printOptionsNoID(options);
	System.out.print(">> Enter your request: ");
	String in = sc.nextLine();
	if (!in.equals("0")) {
	    int i = in.indexOf(' ');
	    String op = in.substring(0, i);
	    String keys = in.substring(i + 1, in.length());
	    DATA = CSVTools.filter_numeric(DATA, op, keys, fields);
	} else {
	    DATA = CSVTools.filter_numeric(DATA, "0", "", fields);
	}
    }

    public static void filter_date() {
	println("Filter Options:");
	String[] options = new String[] { "Month", "Day", "Year" };
	printOptions(options);

	loop: while (true) {
	    int n = promptChoice();
	    int input;
	    String choice = options[n - 1];
	    String key = null;
	    switch (choice) {
	    case "Day":
	    case "Month":
	    case "Year":
		input = promptChoice("Enter " + choice + ":");
		DATA = CSVTools.filter_dates(DATA, input, n);

		break loop;
	    }
	}
    }

    /**************************** AUX METHODS ************************************/
    // crash_data.csv
    private static String[] promptInput(String message) {
	print("\n>> " + message + " ");
	String in = sc.nextLine();
	try {

	    return in.split(" ");
	} catch (Exception e) {
	    println("Something went wrong!");
	    return promptInput(message);
	}
    }

    private static int stckOvflow = 0;

    private static int promptChoice() {
	if (stckOvflow++ > 30)
	    return 7;

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

    private static void printOptionsNoID(String[] S) {
	for (int i = 1; i <= S.length; i++) {
	    println("\t" + S[i - 1]);
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
