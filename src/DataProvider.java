import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DataProvider {

    private static final char NUMBER_OF_INFO_PER_CITY = 3;
    private static final char COORDINATE_X_OF_CITY = 1;
    private static final char COORDINATE_Y_OF_CITY = 2;

    private static final char NUMBER_OF_INFO_PER_ITEM = 4;
    static final char INDEX_OF_ITEM = 0;
    static final char PROFIT_FROM_ITEM = 1;
    static final char WEIGHT_OF_ITEM = 2;
    static final char CITY_OF_ITEM = 3;

    void readFile(String definitionFile) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(definitionFile));
            reader.readLine();//PROBLEM NAME
            reader.readLine();//KNAPSACK DATA TYPE
            DataFromFile.setDimension((int)getNumber(reader.readLine()));
            int numOfItems = (int)getNumber(reader.readLine());
            DataFromFile.setCapacity((int)getNumber(reader.readLine()));
            DataFromFile.setMinSpeed(getNumber(reader.readLine()));
            DataFromFile.setMaxSpeed(getNumber(reader.readLine()));
            DataFromFile.setRentingRatio(getNumber(reader.readLine()));
            reader.readLine();//EDGE_WEIGHT_TYPE
            reader.readLine();//NODE_COORD_SECTION...
            DataFromFile.setCities(new double[DataFromFile.getDimension()][NUMBER_OF_INFO_PER_CITY]);
            DataFromFile.setItems(new int[numOfItems][NUMBER_OF_INFO_PER_ITEM]);
            for (int i = 0; i < DataFromFile.getDimension(); i++) {//filling out cities array
                StringTokenizer st = new StringTokenizer(reader.readLine(), " \t");
                for(int j = 0; j < NUMBER_OF_INFO_PER_CITY; j++) {
                    DataFromFile.getCities()[i][j] = Double.parseDouble(st.nextToken());
                }
            }
            DataFromFile.setDistances(createDistancesArray(DataFromFile.getDimension(), DataFromFile.getCities()));
            reader.readLine();
            for (int i = 0; i < numOfItems; i++) {//filling out items array
                StringTokenizer st = new StringTokenizer(reader.readLine(), " \t");
                for(int j = 0; j < NUMBER_OF_INFO_PER_ITEM; j++) {
                    DataFromFile.getItems()[i][j] = Integer.parseInt(st.nextToken());
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("A file doesn't exist or is in use now!");
        } catch (Exception e) {
            System.out.println("An error has occurred while reading data: " + e);
        }

    }

    private double[][] createDistancesArray(int dimension, double[][] cities) {
        double[][] distances = new double[dimension][dimension];
        for (int i = 0; i < DataFromFile.getDimension(); i++) {
            for (int j = 0; j < DataFromFile.getDimension(); j++) {
                double distance;
                if (i == j) {
                    distance = 0;
                } else {
                    double a = cities[i][COORDINATE_X_OF_CITY] - cities[j][COORDINATE_X_OF_CITY];
                    double b = cities[i][COORDINATE_Y_OF_CITY] - cities[j][COORDINATE_Y_OF_CITY];
                    distance = Math.sqrt( a * a + b * b);
                }
                distances[i][j] = distance;
                distances[j][i] = distance;//redundant
            }
        }
        return distances;
    }

    private double getNumber(String line) {
        Pattern p = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher m = p.matcher(line);
        return m.find() ? Double.parseDouble(m.group()) : -1;
    }
}