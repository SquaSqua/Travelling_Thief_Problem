import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Loader {
    private String definitionFile;
    private int dimension;
    private int numOfItems;
    private int capacity;
    private double minSpeed;
    private double maxSpeed;
    private double rentingRatio;

    public Loader(String definitionFile) {
        this.definitionFile = definitionFile;
    }

    public void readFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(definitionFile));
            System.out.println("Chociaz tu się udalo");
            reader.readLine();//PROBLEM NAME
            reader.readLine();//KNAPSACK DATA TYPE
            dimension = getNumber(reader.readLine()).get(0).intValue();
            numOfItems = getNumber(reader.readLine()).get(0).intValue();
            capacity = getNumber(reader.readLine()).get(0).intValue();
            minSpeed = getNumber(reader.readLine()).get(0);
            maxSpeed = getNumber(reader.readLine()).get(0);
            rentingRatio = getNumber(reader.readLine()).get(0);
            reader.readLine();//EDGE_WEIGHT_TYPE
            System.out.println(dimension + " " + numOfItems + " " + capacity
                    + " " + minSpeed + " " + maxSpeed + " " + rentingRatio);
        } catch (Exception e) {
            System.out.println("The file could not be read!");
        }
    }

    private ArrayList<Double> getNumber(String line) {
        ArrayList<Double> result = new ArrayList<>();
        Pattern p = Pattern.compile("\\d+.\\d+");
        Matcher m = p.matcher(line);
        while (m.find()) {
            result.add(Double.parseDouble(m.group()));
        }
        return result;
    }
}
