public class DataFromFile {

    private static int dimension;
    private static int capacity;
    private static double minSpeed;
    private static double maxSpeed;
    private static double rentingRatio;
    private static double[][] cities;
    private static int[][] items;

    private static double[][] distances;

    public static int getDimension() {
        return dimension;
    }
    public static int getCapacity() {
        return capacity;
    }
    public static int[][] getItems() {
        return items;
    }

    static double getMinSpeed() {
        return minSpeed;
    }
    static double getMaxSpeed() {
         return maxSpeed;
    }
    static double getRentingRatio() {
        return rentingRatio;
    }
    static double[][] getCities() {
        return cities;
    }
    static double[][] getDistances() {
        return distances;
    }

    public static void setDimension(int dimension) {
        DataFromFile.dimension = dimension;
    }
    public static void setCapacity(int capacity) {
        DataFromFile.capacity = capacity;
    }
    public static void setItems(int[][] items) {
        DataFromFile.items = items;
    }

    static void setMinSpeed(double minSpeed) {
        DataFromFile.minSpeed = minSpeed;
    }
    static void setMaxSpeed(double maxSpeed) {
        DataFromFile.maxSpeed = maxSpeed;
    }
    static void setRentingRatio(double rentingRatio) {
        DataFromFile.rentingRatio = rentingRatio;
    }
    static void setCities(double[][] cities) {
        DataFromFile.cities = cities;
    }
    static void setDistances(double[][] distances) {
        DataFromFile.distances = distances;
    }
}
