package GeneticalAlgorithm;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class EncryptionText {

    private static String TEXT_FILE_NAME = "";
    private static String CODE_TEXT_FILE = "";
    private static String CODE_FILE_NAME= "";
    private static Map<Character, Character> CODE_CHAR = new HashMap<>(33);

    EncryptionText(String CODE_FILE_NAME, String TEXT_FILE_NAME, String CODE_TEXT_FILE){
        EncryptionText.TEXT_FILE_NAME = TEXT_FILE_NAME;
        EncryptionText.CODE_TEXT_FILE = CODE_TEXT_FILE;
        EncryptionText.CODE_FILE_NAME = CODE_FILE_NAME;
        try{
        getCodeChar();
        readAndWriteTextFiles();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        }

        private static void readAndWriteTextFiles()throws IOException {
            File codeTextFile = new File(CODE_TEXT_FILE);
            codeTextFile.createNewFile();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                    (new FileInputStream(TEXT_FILE_NAME), StandardCharsets.UTF_8));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter
                    (new FileOutputStream(CODE_TEXT_FILE), StandardCharsets.UTF_8));
            String nextString;

            while ((nextString = bufferedReader.readLine()) != null) {
                nextString = toSimpleString(nextString);    //убираем из исходного текста пробелы и другие разделители
                nextString = toCodeString(nextString);       //кодируем нашим ключом строку
                bufferedWriter.write(nextString);
            }
            bufferedReader.close();
            bufferedWriter.flush();
        }

        private static String toSimpleString(String string){
            string = string.toLowerCase();
            StringBuilder newString = new StringBuilder();
            char[] chars = string.toCharArray();
            for(int i = 0; i < chars.length; i++)
                if(((int) chars[i] >= 1072 && (int) chars[i] <= 1103) || (int) chars[i] == 1105)
                    newString.append(chars[i]);
            return newString.toString();
        }

        private static String toCodeString(String string){
            char[] charset = string.toCharArray();
            StringBuilder newString = new StringBuilder();
            for(int i = 0; i < string.length(); i++)
                newString.append(CODE_CHAR.get(charset[i]));
            return newString.toString();
        }

        private static void getCodeChar()throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                    (new FileInputStream(CODE_FILE_NAME), StandardCharsets.UTF_8));
            char[] codeNums= bufferedReader.readLine().toCharArray();
            for(int i = 1072, q = 0; i <= 1103; i++, q++){
                if(i == 1078){
                    CODE_CHAR.put((char) 1105, codeNums[q]);        //Ключ - нормальный алфавит, значение - закодированный
                    q++;
                }
                CODE_CHAR.put((char)i, codeNums[q]);
            }
        }
    }
