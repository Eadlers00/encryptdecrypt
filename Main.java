package encryptdecrypt;

public class Main {

    public static void main(String[] args) {
        Cipher cipher = new Cipher(args);

        AlgorithmFactory factory = new AlgorithmFactory();
        AlgorithmUsed algorithm = factory.getAlgorithm(cipher.getAlgorithm());
        String result = algorithm.handle(cipher.getMessage(), cipher.getOperation(), cipher.getKey());
        cipher.writeResult(result);
    }

}
