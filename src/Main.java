import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        BigInteger exponent = new BigInteger("1234567890"); // replace with actual exponent
        BigInteger modulus = new BigInteger("9876543210");
        RSAKey senderKey = new RSAKey(exponent, modulus);
        RSAKey receiverKey = new RSAKey(exponent, modulus);

        Transaction transaction = new Transaction();

        byte[] prevTxHash = new byte[32]; // a placeholder hash
        int outputIndex = 0; // the index of the output in the previous transaction
        transaction.addInput(prevTxHash, outputIndex);

        double value = 10.0; // the value of the output in bitcoins
        transaction.addOutput(value, receiverKey);

        transaction.addSignature(transaction.getRawDataToSign(0), 0);

        transaction.finalize();

        System.out.println(transaction);




        Transaction sTran = new Transaction();
        sTran.addInput(transaction.getHash(), 1);
        sTran.addOutput(20, receiverKey);
        sTran.addSignature(sTran.getRawDataToSign(0), 0);

        System.out.println(sTran);

    }
}