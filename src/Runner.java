public class Runner {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Evolution population = new Evolution("src/definitionFiles/hard_4.ttp",1000 ,100);
        population.evolve();
        System.out.println(System.currentTimeMillis() - start);
    }
}
//dopisuję tu jakis komentarz