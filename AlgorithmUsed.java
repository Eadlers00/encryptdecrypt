package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Cipher {

    private String message = "";
    private String algorithm = "shift";
    private String operation = "enc";
    private int key = 0;
    private boolean hasOut = false;
    private File outFile = null;

    public Cipher(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-alg":
                    algorithm = args[i + 1];
                    break;
                case "-mode":
                    operation = args[i + 1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);
                    break;
                case "-data":
                    message = args[i + 1];
                    break;
                case "-in":
                    File file = new File(args[i + 1]);
                    try (Scanner scanner = new Scanner(file)) {
                        while (scanner.hasNext()) {
                            message += scanner.nextLine();
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("Error, could not find the file entered");
                        System.exit(0);
                    }
                    break;
                case "-out":
                    outFile = new File(args[i + 1]);
                    hasOut = true;
                    break;
                default:
                    break;
            }
        }
    }

    public String getMessage() {
        return message;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getOperation() {
        return operation;
    }

    public int getKey() {
        return key;
    }

    public void writeResult(String result) {
        if (hasOut) {
            try (FileWriter writer = new FileWriter(outFile)) {
                writer.write(result);
            } catch (IOException e) {
                System.out.println("Error: problem with the file or input specified");
                System.exit(0);
            }
        } else {
            System.out.print(result);
        }
    }
}

//Algorithm factory in order to choose which algorithm to use, enables further expansion to other methods
class AlgorithmFactory {
    public AlgorithmUsed getAlgorithm(String algorithm) {
        if ("unicode".equals(algorithm)) {
            return new Unicode();
        } else {
            return new Shift();
        }
    }
}

//Interface for handling algorithm
interface AlgorithmUsed {
    String handle(String message, String operation, int key);
}

//Shifting method for encoding and decoding the message
class Shift implements AlgorithmUsed {

    String shift(String message, int offset) {
        StringBuilder encrypted = new StringBuilder();
        for (char character : message.toCharArray()) {
            if (character >= 'a' && character <= 'z') {
                int oldAlphabetPosition = character - 'a';
                int newAlphabetPosition = ((oldAlphabetPosition + offset) % 26) + 'a';
                encrypted.append((char) newAlphabetPosition);
            } else {
                encrypted.append(character);
            }
        }
        return encrypted.toString();
    }

    @Override
    public String handle(String message, String mode, int key) {
        if (mode.equals("enc")) {
            int offset = key % 26;
            return shift(message, offset);
        } else {
            int offset = 26 - (key % 26);
            return shift(message, offset);
        }
    }

}

//Unicode method for encoding and decoding algorithms
    class Unicode implements AlgorithmUsed {
    @Override
    public String handle(String message, String mode, int key) {
        StringBuilder handled = new StringBuilder();
        if (mode.equals("enc")) {
            for (int i = 0; i < message.length(); i++) {
                handled.append(Character.toString(message.charAt(i) + key));
            }
        } else {
            for (int i = 0; i < message.length(); i++) {
                handled.append(Character.toString(message.charAt(i) - key));
            }
        }
        return handled.toString();
    }
}


