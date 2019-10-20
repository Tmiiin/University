package GeneticalAlgorithm;

import java.io.*;

public class Cryptography {

    public static void main(String[] args) throws IOException {
        System.out.println("Выберете что вы хотите сделать:");
        System.out.println("1: Зашифровать текст");
        System.out.println("2: Дешифровать текст");
        switch((Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine()))) {
            case(1): {
                System.out.println("Введите три файла: с шифром, файл в котором хранится текст, файл, в который сохранить результат ");
                new EncryptionText(args[0], args[1], args[2]);
                break;
            }
            case(2): {
                System.out.println("Введите два файла: в котором хранится текст и файл, в который сохранить результат ");
                new DecryptionText(args[0], args[1]);
                break;
            }
            default:
                System.out.println("Извините, но такой опции нет");
        }
    }
}
