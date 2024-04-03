import java.util.*;

public class TxHandler {

	private UTXOPool utxoPool;

	/* Creates a public ledger whose current UTXOPool (collection of unspent
	 * transaction outputs) is utxoPool. This should make a defensive copy of
	 * utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
	 */
	public TxHandler(UTXOPool utxoPool) {
		this.utxoPool = new UTXOPool(utxoPool);
	}

	/* Returns true if
	 * (1) all outputs claimed by tx are in the current UTXO pool,
	 * (2) the signatures on each input of tx are valid,
	 * (3) no UTXO is claimed multiple times by tx,
	 * (4) all of tx’s output values are non-negative, and
	 * (5) the sum of tx’s input values is greater than or equal to the sum of
	        its output values;
	   and false otherwise.
	 */

	public boolean isValidTx(Transaction tx) {
		// IMPLEMENT THIS
		Set<UTXO> claimedUTXOs = new HashSet<UTXO>();
		double inputSum = 0;
		double outputSum = 0;

		List<Transaction.Input> inputs = tx.getInputs();
		for(int i = 0; i < inputs.size(); i++){
			UTXO utxo = new UTXO(inputs.get(i).prevTxHash, inputs.get(i).outputIndex);
			// 1.
			if(!this.utxoPool.contains(utxo)){
				return false;
			}

			// 2.
			Transaction.Output output = utxoPool.getTxOutput(utxo);
			if(!output.address.verifySignature(tx.getRawDataToSign(i), inputs.get(i).signature)){
				return false;
			};

			// 3.
			if(claimedUTXOs.contains(utxo)){
				return false;
			} else {
				claimedUTXOs.add(utxo);
			}

			Transaction.Output utxoOutput = utxoPool.getTxOutput(utxo);
			inputSum += utxoOutput.value;
		}

		List<Transaction.Output> outputs = tx.getOutputs();
        for (Transaction.Output output : outputs) {
            // 4.
            if (output.value < 0) {
                return false;
            }
            outputSum += output.value;
        }

		// 5.
		if(inputSum < outputSum){
			return false;
		}

		return true;
	}

	/* Handles each epoch by receiving an unordered array of proposed
	 * transactions, checking each transaction for correctness,
	 * returning a mutually valid array of accepted transactions,
	 * and updating the current UTXO pool as appropriate.
	 */
	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		List<Transaction> validTxs = new ArrayList<Transaction>();
		for (int i = 0; i < possibleTxs.length; i++) {
			if(isValidTx(possibleTxs[i])){ // returns true if transaction of inputs are valid.
				validTxs.add(possibleTxs[i]);

				List<Transaction.Output> outputs = possibleTxs[i].getOutputs(); // adds the UTXO to the pool
				for(int j = 0; j < outputs.size(); j++){
					UTXO utxo = new UTXO(possibleTxs[i].getHash(), j);
					utxoPool.addUTXO(utxo, outputs.get(j));
				}

				List<Transaction.Input> inputs = possibleTxs[i].getInputs(); // removes the old UTXO from the pool
				for(int j = 0; j < inputs.size(); j++){
					UTXO utxo = new UTXO(inputs.get(j).prevTxHash, inputs.get(j).outputIndex);
					utxoPool.removeUTXO(utxo);
				}
			}
		}

		Transaction[] result = new Transaction[validTxs.size()];
		validTxs.toArray(result);
		return result;
	}

}
