import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Runner {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Evolution population = new Evolution("src/definitionFiles/medium_0.ttp", 50, 100,
                5, 0.6, 0.0005);
        String results = "results.csv";
        try {
            PrintWriter out = new PrintWriter(results);
//            out.println("nr populacji, najgorszy osobnik, najlepszy osobnik, sredni osobnik");
            for (int i = 0; i < population.getNumOfGeners(); i++) {
                out.println(population.evolve(i));
            }
            System.out.println("Najlepszy w ewolucji: " + population.maxOfAll);
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Nie da się utworzyć pliku!");
        }
        System.out.println("Calkowity czas wywolania programu: " + (double)(System.currentTimeMillis() - start) / 1000 + "sek");
    }
}