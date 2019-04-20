import java.time.LocalDate;
import java.time.LocalTime;

public class Runner {
    public static void main(String[] args) {
        String definitionFile = "src/definitionFiles/medium_0.ttp";
        DataProvider loader = new DataProvider();
        loader.readFile(definitionFile);

        long startEvolution = System.currentTimeMillis();
        Evolution population = new Evolution(100, 100,
                5, 0.7,0.01);
        population.run("EVO_" + giveName());
        System.out.println("Calkowity czas wywolania dla ewolucji: " + (double)(System.currentTimeMillis() - startEvolution) / 1000 + "sek");

        long startGreedy = System.currentTimeMillis();
        Greedy closestRoute = new Greedy();
        closestRoute.run("GRE_" + giveName());
        System.out.println("Calkowity czas wywolania dla algorytmu zachłannego: " + (double)(System.currentTimeMillis() - startGreedy) / 1000 + "sek");
    }

    private static String giveName() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String dateAndTime = date.getMonth() + "_" + date.getDayOfMonth()
                + "__" + time.getHour() + "_" + time.getMinute() + "_" + time.getSecond();
        return dateAndTime + "_" + ".csv";
    }
}