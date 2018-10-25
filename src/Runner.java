import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Runner {
    public static void main(String[] args) {
        Evolution population = new Evolution("src/definitionFiles/medium_0.ttp", 50, 100,
                5, 0.6, 0.0005);
        String results = "results.csv";
        try
        {
            PrintWriter out = new PrintWriter(results);
            out.println("ED, PFS, HV");
            out.println(population.evolve());
            out.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Nie da się utworzyć pliku!");
        }
    }
}