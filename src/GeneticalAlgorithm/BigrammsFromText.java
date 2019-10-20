package GeneticalAlgorithm;

import java.util.*;

class BigrammsFromText extends TextAnalyzer{
    private final static double frequencyOfLetterE = 0.00013;
    static Map<String, Double> bigramms = new TreeMap<>();
    static Map<Character, Double> lettersFrequency = new HashMap<>();

    BigrammsFromText(String textFileToRead){
        super(textFileToRead, bigramms, lettersFrequency);
        lettersFrequency.put('ё', frequencyOfLetterE);  //так как оригинальных текстов с буквой ё почти нет,
                                                        //берем ее частоту из интернета
    }
}