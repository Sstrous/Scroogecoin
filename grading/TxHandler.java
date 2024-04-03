import java.util.List;

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

		List<Transaction.Input> inputs = tx.getInputs();
		for(int i = 0; i < inputs.size(); i++){
			// 1.
			UTXO txOutput = new UTXO(inputs.get(i).prevTxHash, inputs.get(i).outputIndex);
			if(!this.utxoPool.contains(txOutput)){
				return false;
			}

			// 2.
			Transaction.Output output = utxoPool.getTxOutput(txOutput);
			if(!output.address.verifySignature(tx.getRawDataToSign(i), inputs.get(i).signature)){
				return false;
			};

			// 3.

		}



		List<Transaction.Output> outputs = tx.getOutputs();
		for(int i = 0; i < outputs.size(); i++){

		}


		return false;
	}

	/* Handles each epoch by receiving an unordered array of proposed
	 * transactions, checking each transaction for correctness,
	 * returning a mutually valid array of accepted transactions,
	 * and updating the current UTXO pool as appropriate.
	 */
	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		// IMPLEMENT THIS
		return null;
	}

}
