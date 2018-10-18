public class Runner {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Evolution population = new Evolution("src/definitionFiles/medium_0.ttp", 100, 10,
                5, 0.1, 0.01);
        population.evolve();
    }
}