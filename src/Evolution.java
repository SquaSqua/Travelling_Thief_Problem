public class Evolution {

    private int dimension;
    private int numOfItems;
    private int capacity;
    private double minSpeed;
    private double maxSpeed;
    private double rentingRatio;
    private City[] cities;
    private Item[] items;

    private double droppingRate;
    private double c;

    public Evolution(String definitionFile, double droppingRate, double c) {
        Loader loader = new Loader(definitionFile);
        loader.readFile();
        dimension = loader.getDimension();
        numOfItems = loader.getNumOfItems();
        capacity = loader.getCapacity();
        minSpeed = loader.getMinSpeed();
        maxSpeed = loader.getMaxSpeed();
        rentingRatio = loader.getRentingRatio();
        cities = loader.getCities();
        items = loader.getItems();

        this.droppingRate = droppingRate;
        this.c = c;
    }

    //public void initialize()
}
