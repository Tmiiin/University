package Lab1;

import java.util.*;

class GeneticalAlgorithm {

    private final static int populationSize = 30;
    private final static char[] alphabetChars = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя".toCharArray();
    private final static int bigSampleNum = 100;

    static ArrayList<Character> defaultAlphabet = new ArrayList<>(alphabetChars.length);
    static Map<ArrayList<Character>, Chromosome> population = new HashMap<>();
    static double bestFitness = bigSampleNum;
    static ArrayList<Character> bestKey;
    private static ArrayList<ArrayList<Character>> bestIndivids;
    private static double sumOfFitness;

    GeneticalAlgorithm() {
        getDefaultAlphabet();
        initializePopulation();
        getFitnessForIndivids();
    }

    Map<ArrayList<Character>, Chromosome> refreshPopulation() {
        Map<ArrayList<Character>, Chromosome> newPopulation = new HashMap<>(population.size());
        getBestIndivids(); //25% исходной популяции
        for (ArrayList<Character> array : bestIndivids)
            newPopulation.put(array, new Chromosome());

        getMutationPopulation(newPopulation);                            //50% мутаций и скрещиваний

        while (newPopulation.size() != population.size())                 //25% новых особей
            newPopulation.put(shuffle(new ArrayList<>(defaultAlphabet)), new Chromosome());

        sumOfFitness = 0;

        for (Map.Entry entry : newPopulation.entrySet())                //найти значение фитнес-функций для новых индивидов
            fitnessFunction(entry);
        bestIndivids.clear();
        return newPopulation;
    }

    private void getMutationPopulation(Map<ArrayList<Character>, Chromosome> newPopulation) {
        ArrayList<Character> firstKey, secondKey;
        ArrayList<Character> bestIndivid = bestIndivids.get((int) (Math.random() * (bestIndivids.size() - 1)));
        while (newPopulation.size() < population.size() / 2) {                              //25% от исходной популяции
            newPopulation.put(mutation(bestIndivid), new Chromosome());
        }                                                             // подвергаем ее мутации и добавляем в популяцию
        while (newPopulation.size() < (population.size() / 2 + population.size() / 4)) {
            firstKey = bestIndivid;
            secondKey = rouletteWheel();
            if (!(firstKey.equals(secondKey))) {
                crossbreeding(firstKey, secondKey);
                newPopulation.put(firstKey, new Chromosome());
                newPopulation.put(secondKey, new Chromosome());
            }
        }
    }

    private ArrayList<Character> rouletteWheel() {
        ArrayList<Character> q = new ArrayList<>();
        int sum = 0;
        int randomNum = (int) (Math.random() * sumOfFitness);
        for (Map.Entry entry : population.entrySet()) {
            q.addAll((ArrayList<Character>)entry.getKey());
            if (sum < randomNum) {
                sum += ((Chromosome) entry.getValue()).getVeight();
            } else return q;
            q.clear();
        }
        return shuffle((ArrayList<Character>) defaultAlphabet.clone());
    }

    private void crossbreeding(ArrayList<Character> first1, ArrayList<Character> second2) { //partially-mapped crossover
        ArrayList<Character> partOfFirst; //отрезки которые между двемя хромосомами
        ArrayList<Character> partOfSecond;//мы меняем местами
        Map<Character, Character> mappingForFirst = new HashMap<>();
        Map<Character, Character> mappingForSecond = new HashMap<>();
        ArrayList<Character> first = new ArrayList<>(first1);
        ArrayList<Character> second = new ArrayList<>(second2);

        int[] q = getRandomNums(first.size());
        int randomNum1 = q[0];
        int randomNum2 = q[1];

        partOfFirst = new ArrayList<>(first.subList(randomNum1, randomNum2));
        partOfSecond = new ArrayList<>(second.subList(randomNum1, randomNum2));
        for (int i = 0; i < randomNum2 - randomNum1; i++) {
            mappingForFirst.put(partOfFirst.get(i), partOfSecond.get(i));
            mappingForSecond.put(partOfSecond.get(i), partOfFirst.get(i));
        }

        first1 = check(first, mappingForSecond, randomNum1, randomNum2, partOfSecond); //проверяем для крайних частей неповторение элементов
        second2 = check(second, mappingForFirst, randomNum1, randomNum2, partOfFirst);

    }

    private void getKey(ArrayList<Character> first, ArrayList<Character> second, ArrayList<Character> third, ArrayList<Character> key) {
        key.clear();
        key.addAll(first);
        key.addAll(second);
        key.addAll(third);
    }

    private ArrayList<Character> check(ArrayList<Character> key, Map<Character, Character> mapping, int randomNum1, int randomNum2, ArrayList<Character> partOfOtherKey) {
        ArrayList<Character> firstPartOfKey = new ArrayList<>(key.subList(0, randomNum1));
        ArrayList<Character> thirdPartOfKey = new ArrayList<>(key.subList(randomNum2, key.size()));

        getMappingForAllElements(firstPartOfKey, mapping);
        getMappingForAllElements(thirdPartOfKey, mapping);

        getKey(firstPartOfKey, partOfOtherKey, thirdPartOfKey, key);
        return key;
    }

    private ArrayList<Character> mutation(ArrayList<Character> chromosome) {
        int[] randomNums = getRandomNums(chromosome.size());
        int randomNum1 = randomNums[0];
        int randomNum2 = randomNums[1];
        Character q;
        ArrayList<Character> newIndivid = new ArrayList<>(chromosome);
        q = newIndivid.get(randomNum1);
        newIndivid.set(randomNum1, newIndivid.get(randomNum2));
        newIndivid.set(randomNum2, q);

        return newIndivid;
    }

    private void getBestIndivids() {
        Double[] fitnessFunction = new Double[population.size()];
        int i = 0;
        bestIndivids = new ArrayList<>();
        for (Map.Entry entry : population.entrySet())
            fitnessFunction[i++] = ((Chromosome) entry.getValue()).getVeight();

        Arrays.sort(fitnessFunction);
        while (bestIndivids.size() < population.size() / 4)
            for (Map.Entry entry : population.entrySet()) {
                for (i = 0; i < (population.size() / 4); i++) {
                    if (((Chromosome) entry.getValue()).getVeight() == fitnessFunction[i]) {
                        bestIndivids.add((ArrayList<Character>)entry.getKey());
                        break;
                    }
                }
            }
    }

    private void fitnessFunction(Map.Entry entry) {
        Chromosome guy = (Chromosome) entry.getValue();
        decryptionWithKey((ArrayList<Character>) entry.getKey(), guy);
        guy.setVeight(guy.getBigramFrequency() + guy.getLetterFrequency());
        sumOfFitness += guy.getVeight();
        isBestIndivid(entry);
    }

    private void isBestIndivid(Map.Entry entry){
        if (bestFitness > ((Chromosome) entry.getValue()).getVeight()) {
            bestFitness = ((Chromosome) entry.getValue()).getVeight();
            bestKey = (ArrayList<Character>) entry.getKey();
        }
    }

    private void decryptionWithKey(ArrayList<Character> alphabet, Chromosome chromosome) {
        double bigrammsFrequency = 0;
        double letterFrequency = 0;

        for (int i = 0; i < defaultAlphabet.size(); i++) {
            double frequencyOfNewLetter = DecryptionText.textFrequencyOfLetter.get(defaultAlphabet.get(i));
            letterFrequency += Math.abs(frequencyOfNewLetter - BigrammsFromText.lettersFrequency.get(alphabet.get(i)));
        }

        for (String bigram : DecryptionText.textBigrammsFrequency.keySet()) {
            String q = decryptString(bigram, alphabet);
            if (BigrammsFromText.bigramms.containsKey(q))
                bigrammsFrequency += Math.abs(DecryptionText.textBigrammsFrequency.get(bigram) - BigrammsFromText.bigramms.get(q));
        }

        chromosome.setBigramFrequency(bigrammsFrequency);
        chromosome.setLetterFrequency(letterFrequency);
    }

    private String decryptString(String bi, ArrayList<Character> alphabet) {
        char[] q = bi.toCharArray();
        StringBuilder w = new StringBuilder();
        int t = 0, z = 0;
        for (int i = 0; i < defaultAlphabet.size(); i++) {
            if (q[0] == defaultAlphabet.get(i)) z = i;
            if (q[1] == defaultAlphabet.get(i)) t = i;
        }
        q[0] = alphabet.get(z);
        q[1] = alphabet.get(t);

        w.append(q[0]);
        w.append(q[1]);
        return w.toString();
    }

    private static ArrayList<Character> shuffle(ArrayList<Character> alphabet) {
        int last = alphabet.size();
        for (int i = alphabet.size() - 1; i >= 1; i--) {
            int randomNum = (int) (Math.random() * last);
            int q = alphabet.get(randomNum);
            alphabet.set(randomNum, alphabet.get(i));
            alphabet.set(i, (char) q);
        }
        return alphabet;
    }

    private void getMappingForAllElements(List<Character> partOfKey, Map<Character, Character> mapping) {
        for (int i = 0; i < partOfKey.size(); i++)
            if (mapping.containsKey(partOfKey.get(i)))
                partOfKey.set(i, getMapping(mapping, partOfKey.get(i)));
    }

    private int[] getRandomNums(int firstLength) {
        int randomNum1 = (int) (Math.random() * (firstLength - 1)) + 1;
        int randomNum2 = (int) (Math.random() * (firstLength - 1)) + 1;
        int[] nums = {randomNum1, randomNum2};
        if (randomNum1 == randomNum2)
            randomNum1 = (int) (Math.random() * (firstLength - 1)) + 1;//если промежуток == 1, расширяем
        if (randomNum1 > randomNum2) {                                           //строим отображение из первого отрезка во второй
            nums[1] = randomNum1;
            nums[0] = randomNum2;
            return nums;
        }
        return nums;
    }

    private Character getMapping(Map<Character, Character> map, char character) {
        while (map.containsKey(character)) {
            character = map.get(character);
        }
        return character;
    }

    private static void getDefaultAlphabet(){
        for (char c : alphabetChars) defaultAlphabet.add(c);
    }

    private static void initializePopulation(){
        for (int i = 0; i < populationSize; i++)
            population.put(shuffle(new ArrayList<>(defaultAlphabet)), new Chromosome());
    }

    private void getFitnessForIndivids(){
        for (Map.Entry entry : population.entrySet()) {
            fitnessFunction(entry);
        }
    }
}