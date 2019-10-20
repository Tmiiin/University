package Lab1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

class TextAnalyzer {
    final static HashSet<Character> alphabet = new HashSet<>();
    private static char[] alphabetChars = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя".toCharArray();
    private Map<String, Double> bigramms;
    private Map<Character, Double> lettersFrequency;
    private int sumOfLetters;

    static{
        getDefaultAlphabet();
    }

    private static void getDefaultAlphabet(){
        for (char c : alphabetChars) alphabet.add(c);
    }

    TextAnalyzer(String textFileToRead, Map<String, Double> bigramms, Map<Character, Double> lettersFrequency){
        this.bigramms = bigramms;
        this.lettersFrequency = lettersFrequency;
        try{
            readAndProcessingTextFiles(textFileToRead);
        }
        catch(IOException w){ w.printStackTrace();}
    }


    private void readAndProcessingTextFiles(String textFileToRead) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                (new FileInputStream(textFileToRead), StandardCharsets.UTF_8));
        String nextString;
        initializeLetters(lettersFrequency);
        while ((nextString = bufferedReader.readLine()) != null) {
            nextString = toSimpleString(nextString);    //убираем из исходного текста пробелы и другие разделители
            if(!nextString.isEmpty())
                setNumberOfLettersAndBigramms(nextString.toCharArray());
        }
        getPercentOfBigramsAndLetters();
        bufferedReader.close();
    }

    private String toSimpleString(String string) {
        string = string.toLowerCase();
        StringBuilder newString = new StringBuilder();
        char[] chars = string.toCharArray();
        for (char aChar : chars)
            if (alphabet.contains(aChar)) {
                newString.append(aChar);
                sumOfLetters++;
            }
        return newString.toString();
    }

    private void getPercentOfBigramsAndLetters(){
        for (Map.Entry entry : (new TreeMap<>(bigramms)).entrySet()) {
            double percentOfBigramms = ((double) entry.getValue() / (double) sumOfLetters);
            bigramms.put((String) entry.getKey(), percentOfBigramms);
        }

        for (Map.Entry entry : (new TreeMap<>(lettersFrequency)).entrySet()) {
            lettersFrequency.put((Character) entry.getKey(), (double) entry.getValue() / (double) sumOfLetters);
        }
    }

    private void setNumberOfLettersAndBigramms(char[] charOfCodeString) {
        StringBuilder bi = new StringBuilder();
        bi.append(charOfCodeString[0]);

        for (char character : charOfCodeString) {
            lettersFrequency.put(character, lettersFrequency.get(character) + 1.0);
            bi.append(character);
            if (bigramms.containsKey(bi.toString())) {
                bigramms.put(bi.toString(), bigramms.get(bi.toString()) + 1.0);
            } else bigramms.put(bi.toString(), 1.0);
            bi.setLength(0);
            bi.append(character);
        }
    }

    private void initializeLetters(Map<Character, Double> map) {
        for (Character i : alphabet)
            map.put(i, 0.0);
    }
}
