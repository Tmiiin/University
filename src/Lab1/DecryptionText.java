package Lab1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class DecryptionText {
    private final static int timesToRefresh = 2000;
    private final static String textFileToRead = "wapAndPeace.txt";
    private static String TEXT_FILE_NAME = "";
    private static String ENCRYPTION_TEXT_FILE = "";
    private static TextAnalyzer thisText;
    static Map<Character, Double> textFrequencyOfLetter = new HashMap<>();
    static Map<String, Double> textBigrammsFrequency = new TreeMap<>();

    DecryptionText(String TEXT_FILE_NAME, String ENCRYPTION_TEXT_FILE) throws IOException, NullPointerException {
        DecryptionText.TEXT_FILE_NAME = TEXT_FILE_NAME;
        DecryptionText.ENCRYPTION_TEXT_FILE = ENCRYPTION_TEXT_FILE;
        initializeDefaultBigrammsFrequency();
        frequencyLetter();
        GeneticalAlgorithm ga = new GeneticalAlgorithm();

        for (int i = 0; i < timesToRefresh; i++)
            GeneticalAlgorithm.population = ga.refreshPopulation();
        System.out.println(GeneticalAlgorithm.bestFitness);
        decryptionWithKey(GeneticalAlgorithm.bestKey);
    }

    private static void decryptionWithKey(ArrayList<Character> alphabet) throws IOException {
        int nextChar;
        File file = new File(ENCRYPTION_TEXT_FILE);
        file.createNewFile();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                (new FileInputStream(TEXT_FILE_NAME), StandardCharsets.UTF_8));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream(file), StandardCharsets.UTF_8));

        while ((nextChar = bufferedReader.read()) != -1) {
            bufferedWriter.write(alphabet.get(decryptionLetter(nextChar)));
        }
        bufferedWriter.flush();
        bufferedReader.close();                                         //расшифровали текст с помощью возможного ключа

    }

    private static int decryptionLetter(int letter) {
        for (int i = 0; i < GeneticalAlgorithm.defaultAlphabet.size(); i++)
            if (GeneticalAlgorithm.defaultAlphabet.get(i) == (char) letter)
                return i;
        return 0;
    }

    private void frequencyLetter() {
        thisText = new TextAnalyzer(TEXT_FILE_NAME, textBigrammsFrequency, textFrequencyOfLetter);
    }

    private static void initializeDefaultBigrammsFrequency() {
        new BigrammsFromText(textFileToRead);
    }
}