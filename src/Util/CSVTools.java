package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import javafx.util.converter.LocalDateStringConverter;

public class CSVTools {

    public static final String[] ARRAY_FACTORY = new String[0];

    @SuppressWarnings("deprecation")
    public static List<String[]> read(String dataPath) throws IOException {
	Reader in = new FileReader(dataPath);
	String[] headers = getHeaders(dataPath);
	Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(headers).withFirstRecordAsHeader().parse(in);
	List<String[]> retval = new ArrayList<String[]>();
	retval.add(headers);
	for (CSVRecord record : records) {
	    retval.add(record.toList().toArray(ARRAY_FACTORY));
	}
	return retval;
    }

    public static boolean[] crossCompareArrayElements(Object[] master, Object[] slave, boolean what2fill) {
	boolean[] res = new boolean[master.length];
	Arrays.fill(res, !what2fill); // fill flag array
	for (int i = 0; i < res.length; i++) {
	    for (int j = 0; j < slave.length; j++) {
		if (master[i].equals(slave[j])) {
		    res[i] = what2fill;
		}
	    }
	}
	return res;
    }

    public static List<String[]> selectByField(List<String[]> data, boolean inclusive, String... fields) {
	List<String[]> retval = new ArrayList<>();
	String[] oldHeaders = data.get(0);

	/* Define what fields are going to be used */
	boolean[] includeHeaders = crossCompareArrayElements(oldHeaders, fields, inclusive);

	/* Add Headers */
	ArrayList<String> newHeaders = new ArrayList<>();
	for (int i = 0; i < includeHeaders.length; i++) {
	    if (!includeHeaders[i])
		continue;
	    newHeaders.add(oldHeaders[i]);
	}
	retval.add(newHeaders.toArray(ARRAY_FACTORY));

	/* Start filling the resulting list */
	for (int j = 1; j < data.size(); j++) {
	    String[] row = data.get(j);
	    ArrayList<String> newRow = new ArrayList<>();

	    for (int i = 0; i < includeHeaders.length; i++) {
		if (!includeHeaders[i])
		    continue;
		newRow.add(row[i]);
	    }

	    retval.add(newRow.toArray(ARRAY_FACTORY));
	}
	return retval;
    }

    public static List<String[]> selectByRange(List<String[]> data, int end, boolean inclusive) {
	return selectByRange(data, 1, end, inclusive);
    }

    public static List<String[]> selectByRange(List<String[]> data, int start, int end, boolean inclusive) {
	/* Just to increase quality of life */
	if (start < 1)
	    start = 1;
	if (end >= data.size())
	    end = data.size() - 1;

	List<String[]> retval = new ArrayList<>();
	retval.add(data.get(0));
	for (int i = 1; i < data.size(); i++) {
	    boolean cond = i >= start && i <= end;
	    if ((inclusive) ? cond : !cond) {
		retval.add(data.get(i));
	    }
	}

	return retval;
    }

    public static List<String[]> selectByRangeAndFields(List<String[]> data, boolean inclusive, int end,
	    String... fields) throws FileNotFoundException {
	/* Just to increase quality of life */
	if (fields == null || fields.length == 0)
	    fields = getHeaders(data);

	List<String[]> retval = selectByRange(data, 1, end, inclusive);
	retval = selectByField(data, inclusive, fields);
	return retval;
    }

    public static List<String[]> selectByRangeAndFields(List<String[]> data, boolean inclusive, int start, int end,
	    String... fields) {
	List<String[]> retval = selectByRange(data, start, end, inclusive);
	retval = selectByField(data, inclusive, fields);
	return retval;
    }

    public static void display(final List<String[]> entries) {
	display(entries, 0, entries.size() - 1);
    }

    public static void display(final List<String[]> entries, int total) {
	display(entries, 0, Math.min(entries.size() - 1, total));
    }

    public static void display(final List<String[]> entries, int start, int end) {
	System.out.println("[ID]" + Arrays.toString(entries.get(0))); // show headers
	start = (start == 0) ? 1 : start; // exclude headers
	for (int i = start; i < end + 1; i++) {
	    System.out.print("[" + i + "][" + entries.get(i)[0]);
	    for (int j = 1; j < entries.get(i).length; j++) {
		String val = entries.get(i)[j];
		try {
		    Double.parseDouble(val);
		} catch (Exception e) {
		    val = "\"" + val + "\"";
		}
		System.out.print(", " + val);
	    }
	    System.out.println("]");
	}
	System.out.println((entries.size() - 1) + " entries...");

    }

    public static void displayRow(String[] row) {
	String printval = "{\n";
	for (String str : row)
	    printval += "\t" + str + "\n";

	printval += "}\n";
	System.out.println(printval);
    }

    public static String[] getHeaders(List<String[]> data) throws FileNotFoundException {
	return data.get(0);
    }

    public static String[] getHeaders(String path) throws FileNotFoundException {
	try (Scanner sc = new Scanner(new File(path))) {
	    String readline = sc.nextLine();
	    return readline.split(",");
	}
    }

    /**
     * FIXME: NOT TESTED
     */
    public static List<String[]> search(List<String[]> rows, String field, String value) {

	// init
	String[] headers = rows.get(0);
	List<String[]> retval = new ArrayList<>();
	retval.add(headers);

	// search field name
	int f_index = Helper.getStringIndex(headers, field);
	if (f_index == -1)
	    return retval;

	// search value at that field
	for (int i = 1; i < rows.size(); i++) {
	    String[] row = rows.get(i);
	    try {
		// if it is numeric look for exact match
		if (Double.parseDouble(row[f_index]) == Double.parseDouble(value)) {
		    retval.add(row);
		}
	    } catch (Exception e) {
		// if the parsing fails, that means it is not numeric. So, use contains.
		if (row[f_index].contains(value)) {
		    retval.add(row);
		}

	    }

	}

	return retval;
    }

    public static String convertToBiggerDate(String date) {

	int l1 = date.lastIndexOf("/");
	String s1 = date.substring(0, l1);
	String s2 = date.substring(l1 + 1, l1 + 3);
	int n = Integer.parseInt(s2);
	if (n < 30)
	    n += 2000;
	else
	    n += 1900;

	return s1 + "/" + n;

    }

    public static String convertToBiggerTime(String date) {

	if (date.equals(""))
	    return "";

	String[] s = date.split(":");

	if (s[0].length() == 1)
	    s[0] = "0" + s[0];

	if (s[1].length() == 1)
	    s[1] = "0" + s[1];

	return s[0] + ":" + s[1];

    }

    public static List<String[]> sort(List<String[]> rows, String field, boolean asc) {
	int index = Helper.getStringIndex(rows.get(0), field);
	String[] headers = rows.remove(0);
	rows.sort(new Comparator<String[]>() {
	    @Override
	    public int compare(String[] s1, String[] s2) {
		String so1 = ((asc) ? s1 : s2)[index];
		String so2 = ((asc) ? s2 : s1)[index];
		if (field.equals("Date")) {
		    so1 = CSVTools.convertToBiggerDate(so1);
		    so2 = CSVTools.convertToBiggerDate(so2);
		    LocalDate d1 = new LocalDateStringConverter().fromString(so1);
		    LocalDate d2 = new LocalDateStringConverter().fromString(so2);
		    return d1.compareTo(d2);
		}

		if (field.equals("Time")) {
		    return convertToBiggerTime(so1).compareTo(convertToBiggerTime(so2));
		}

		return so1.compareTo(so2);

	    }
	});
	rows.add(0, headers);
	return rows;
    }

    /**
     * Map should contain the name of the field as the key. The values of the Map
     * are regular expression. <br>
     * Here are the regex list:<br>
     * "^[x].+" = starts with <br>
     * "[a-z]+[x]$" = ends with <br>
     * "^.*Test.*$" = contains <br>
     * 
     */
    public static List<String[]> filter_string(List<String[]> rows, String regex, String... fields) {
	if (regex == null || regex.isEmpty())
	    return rows;
	if (fields == null || fields.length == 0)
	    return rows;
	/* Define what fields are going to be used */
	String[] headers = rows.get(0);
	boolean[] includeHeaders = crossCompareArrayElements(headers, fields, true);

	List<String[]> retval = new ArrayList<>();
	retval.add(headers);
	outer: for (int i = 1; i < rows.size(); i++) {
	    String[] row = rows.get(i);

	    for (int j = 0; j < includeHeaders.length; j++) {
		if (!includeHeaders[j])
		    continue;

		if (row[j].matches(regex)) { // FIXME: potential ambiquity
		    retval.add(row);
		    continue outer;
		}
	    }

	}

	return retval;
    }

    public static List<String[]> filterNulls(List<String[]> rows, boolean getNulls, String... fields) {
	if (fields == null || fields.length == 0)
	    return rows;
	/* Define what fields are going to be used */
	String[] headers = rows.get(0);
	boolean[] includeHeaders = crossCompareArrayElements(headers, fields, true);

	List<String[]> retval = new ArrayList<>();
	retval.add(headers);
	outer: for (int i = 1; i < rows.size(); i++) {
	    String[] row = rows.get(i);

	    for (int j = 0; j < includeHeaders.length; j++) {
		if (!includeHeaders[j])
		    continue;
		boolean cond = row[j] == null || row[j].equals("");
		cond = (getNulls) ? cond : !cond;
		if (cond) {
		    retval.add(row);
		    continue outer;
		}
	    }

	}

	return retval;
    }

    /* Between 1933 - 2009 */
    private static String formatIfDate(String a) {
	String splt[] = a.split("/");
	for (int i = 0; i < 3; i++)
	    splt[i] = ((splt[i].length() == 1) ? "0" : "") + splt[i];

	if (stringCompare(splt[2], "33 99", "<>"))
	    splt[2] = "19" + splt[2];
	else
	    splt[2] = "20" + splt[2];

	return splt[0] + "/" + splt[1] + "/" + splt[2];

    }

    public static boolean stringCompare(String a, String b, String op) {
	a = formatIfDate(a);
	b = formatIfDate(b);
	int res = a.compareTo(b);
	switch (op) {
	case ">": // gt
	    return res > 0;
	case ">=": // gte
	    return res >= 0;
	case "<": // lt
	    return res < 0;
	case "<=": // lte
	    return res <= 0;
	case "=": // lte
	    return res == 0;
	case "<>":
	    String[] nums = b.split(" ");
	    return a.compareTo(nums[0]) >= 0 && a.compareTo(nums[1]) <= 0;
	default:
	    return res == 0;
	}
    }

    public static List<String[]> filter_numeric(List<String[]> rows, String op, String key, String... fields) {
	if (fields == null || fields.length == 0)
	    return rows;
	/* Define what fields are going to be used */
	String[] headers = rows.get(0);
	boolean[] includeHeaders = crossCompareArrayElements(headers, fields, true);

	List<String[]> retval = new ArrayList<>();
	retval.add(headers);
	outer: for (int i = 1; i < rows.size(); i++) {
	    String[] row = rows.get(i);

	    for (int j = 0; j < includeHeaders.length; j++) {
		if (!includeHeaders[j])
		    continue;
		boolean cond = true;
		if (cond) {
		    retval.add(row);
		    continue outer;
		}
	    }

	}

	return retval;
    }

    private static String formatRegexString(String re) {
	String newre = "";
	char[] chars = re.toCharArray();
	for (char ch : chars) {
	    if ("[$&+,:;=?@#|'<>.-^*()%!]".contains(ch + "")) {
		newre += "\\";
	    }
	    newre += ch;
	}
	return newre;
    }

    public static String getStartsWithRegex(String subs) {
	subs = formatRegexString(subs);
	return "^".concat(subs).concat(".*$");
    }

    public static String getEndsWithRegex(String subs) {
	subs = formatRegexString(subs);
	return "^.*".concat(subs).concat("$");
    }

    public static String getContainsRegex(String subs) {
	subs = formatRegexString(subs);
	return "^.*".concat(subs).concat(".*$");
    }

}

class Helper {

    public static String minimize(String str) {
	return str.toLowerCase().trim();
    }

    public static int getStringIndex(String[] arr, String str) {

	for (int i = 0; i < arr.length; i++) {
	    if (minimize(arr[i]).equals(minimize(str))) {
		return i;
	    }
	}
	return -1;
    }
}
