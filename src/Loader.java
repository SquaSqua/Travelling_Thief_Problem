import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.StringTokenizer;
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
    private double[][] cities;
    private int[][] items;

    public Loader(String definitionFile) {
        this.definitionFile = definitionFile;
    }

    public void readFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(definitionFile));
            reader.readLine();//PROBLEM NAME
            reader.readLine();//KNAPSACK DATA TYPE
            dimension = (int)getNumber(reader.readLine());
            numOfItems = (int)getNumber(reader.readLine());
            capacity = (int)getNumber(reader.readLine());
            minSpeed = getNumber(reader.readLine());
            maxSpeed = getNumber(reader.readLine());
            rentingRatio = getNumber(reader.readLine());
            reader.readLine();//EDGE_WEIGHT_TYPE
            reader.readLine();//NODE_COORD_SECTION...
            cities = new double[dimension][3];
            items = new int[numOfItems][4];
            for (int i = 0; i < dimension; i++) {//filling out cities array
                StringTokenizer st = new StringTokenizer(reader.readLine(), " \t");
                for(int j = 0; j < 3; j++) {
                    cities[i][j] = Double.parseDouble(st.nextToken());
                }
            }
            reader.readLine();
            for (int i = 0; i < numOfItems; i++) {//filling out items array
                StringTokenizer st = new StringTokenizer(reader.readLine(), " \t");
                for(int j = 0; j < 4; j++) {
                    items[i][j] = Integer.parseInt(st.nextToken());
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("A file doesn't exist or is in use now!");
        } catch (Exception e) {
            System.out.println("An error has occurred while reading data: " + e);
        }

    }

    private double getNumber(String line) {
        Pattern p = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher m = p.matcher(line);
        m.find();
        return Double.parseDouble(m.group());
    }


    public int getDimension() { return dimension; }
    public int getNumOfItems() { return numOfItems; }
    public int getCapacity() { return capacity; }
    public double getMinSpeed() { return minSpeed; }
    public double getMaxSpeed() { return maxSpeed; }
    public double getRentingRatio() { return rentingRatio; }
    public double[][] getCities() { return cities; }
    public int[][] getItems() { return items; }
}