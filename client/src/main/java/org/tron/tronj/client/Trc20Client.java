package org.tron.tronj.client;

/**
 * The {@code Trc20Client} class wraps the TRC-20 standard function calls defined in TIP-20.
 * 
 * <p> The {@code Trc20Client} class includes already written smart contract functions. You may call 
 * these functions simply by the methods inside the {@code TronClient} class.
 * Note that the function whose name starts with 'get' are constant calls.</p>
 * 
 * @since jdk13.0.2+8
 * @see org.tron.tronj.client.TronClient
 * @see org.tron.tronj.abi.datatypes.Function
 */

import org.tron.tronj.abi.datatypes.Address;
import org.tron.tronj.abi.datatypes.Bool;
import org.tron.tronj.abi.datatypes.Function;
import org.tron.tronj.abi.datatypes.generated.Uint8;
import org.tron.tronj.abi.datatypes.generated.Uint256;
import org.tron.tronj.abi.datatypes.Utf8String;
import org.tron.tronj.abi.FunctionReturnDecoder;
import org.tron.tronj.abi.TypeReference;
import org.tron.tronj.client.Transaction.TransactionBuilder;
import org.tron.tronj.client.TronClient;
import org.tron.tronj.proto.Chain.Transaction;
import org.tron.tronj.proto.Response.TransactionExtention;
import org.tron.tronj.proto.Response.TransactionReturn;
import org.tron.tronj.utils.Numeric;

import com.google.protobuf.ByteString;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

 public class Trc20Client {
     private TronClient client;

     public Trc20Client(String grpcEndpoint, String grpcEndpointSolidity, String hexPrivateKey) {
         client = new TronClient(grpcEndpoint, grpcEndpointSolidity, hexPrivateKey);
     }

     public static Trc20Client ofMainnet(String hexPrivateKey) {
         return new Trc20Client("grpc.trongrid.io:50051", "grpc.trongrid.io:50052",hexPrivateKey);
     }

     public static  Trc20Client ofShasta(String hexPrivateKey) {
         return new Trc20Client("grpc.shasta.trongrid.io:50051", "grpc.shasta.trongrid.io:50052", hexPrivateKey);
     }

     public static Trc20Client ofNile(String hexPrivateKey) {
        return new Trc20Client("47.252.19.181:50051", "47.252.19.181:50061", hexPrivateKey);
     }

     /**
      * Call function name() public view returns (string).
      *
      * Returns the name of the token - e.g. "MyToken".
      *
      * @param callerAddr The caller's address
      * @param cntrAddr The contract's address
      * @return A TransactionExtention object contains constant result to resolve
      */
     public String getName(String callerAddr, String cntrAddr) throws Exception{
        Function name = new Function("name",
                Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));

        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, name);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (String)FunctionReturnDecoder.decode(result, name.getOutputParameters()).get(0).getValue();
     }

     /**
      * Call function symbol() public view returns (string).
      *
      * Returns the symbol of the token. E.g. "HIX".
      *
      * @param callerAddr The caller's address
      * @param cntrAddr The contract's address
      * @return A TransactionExtention object contains constant result to resolve
      */
      public String getSymbol(String callerAddr, String cntrAddr) throws Exception {
        Function symbol = new Function("symbol",
                Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));

        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, symbol);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (String)FunctionReturnDecoder.decode(result, symbol.getOutputParameters()).get(0).getValue();
      }

      /**
       * Call function decimals() public view returns (uint8).
       * 
       * Returns the number of decimals the token uses - e.g. 8, 
       * means to divide the token amount by 100000000 to get its user representation
       * 
       * @param callerAddr The caller's address
       * @param cntrAddr The contract's address
       * @return A TransactionExtention object contains constant result to resolve
       */
      public BigInteger getDecimals(String callerAddr, String cntrAddr) throws Exception {
        Function decimals = new Function("decimals",
                Collections.emptyList(), Arrays.asList(new TypeReference<Uint8>() {}));
        
        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, decimals);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, decimals.getOutputParameters()).get(0).getValue();
      }

      /**
       * Call function totalSupply() public view returns (uint256).
       * 
       * Returns the total token supply.
       * 
       * @param callerAddr The caller's address
       * @param cntrAddr The contract's address
       * @return A TransactionExtention object contains constant result to resolve
       */
      public BigInteger getTotalSupply(String callerAddr, String cntrAddr) throws Exception {
        Function totalSupply = new Function("totalSupply",
                Collections.emptyList(), Arrays.asList(new TypeReference<Uint256>() {}));

        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, totalSupply);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, totalSupply.getOutputParameters()).get(0).getValue();
      }

      /**
       * Call function balanceOf(address _owner) public view returns (uint256 balance).
       * 
       * Returns the account balance of another account with address _owner.
       * 
       * @param ownerAddr The token owner's address
       * @param callerAddr The caller's address
       * @param cntrAddr The contract's address
       * @return A TransactionExtention object contains constant result to resolve
       */
      public BigInteger getBalanceOf(String ownerAddr, String callerAddr, String cntrAddr) throws Exception {
        Function balanceOf = new Function("balanceOf",
                Arrays.asList(new Address(ownerAddr)), Arrays.asList(new TypeReference<Uint256>() {}));

        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, balanceOf);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, balanceOf.getOutputParameters()).get(0).getValue();
      }

      /**
       * Call function transfer(address _to, uint256 _value) public returns (bool success).
       * 
       * Transfers _value amount of tokens to address _to.
       * 
       * @param destAddr The address to receive the token
       * @param amount The transfer amount
       * @param callerAddr The caller's address
       * @param cntrAddr The contract's address
       * @param memo The transaction memo
       * @param feeLimit The energy fee limit
       * @return A TransactionReturn object contains the trigger result(true / false)
       */
      public TransactionReturn transfer(String destAddr, long amount, 
             String callerAddr, String cntrAddr, String memo, long feeLimit) throws Exception {
        Function transfer = new Function("transfer",
                Arrays.asList(new Address(destAddr),
                        new Uint256(BigInteger.valueOf(amount).multiply(BigInteger.valueOf(10).pow(18)))),
                Arrays.asList(new TypeReference<Bool>() {}));

        TransactionBuilder builder = client.triggerCall(callerAddr, cntrAddr, transfer);
        builder.setFeeLimit(feeLimit);
        builder.setMemo(memo);

        Transaction signedTxn = client.signTransaction(builder.build());
        return client.broadcastTransaction(signedTxn);
      }

      /**
       * call function transferFrom(address _from, address _to, uint256 _value) public returns (bool success)
       * 
       * The transferFrom method is used for a withdraw workflow, 
       * allowing contracts to transfer tokens on your behalf. This can only be called
       * when someone has allowed you some amount.
       * 
       * @param fromAddr The address who sends tokens (or the address to withdraw from)
       * @param destAddr The address to receive the token
       * @param amount The transfer amount
       * @param callerAddr The caller's address
       * @param cntrAddr The contract's address
       * @param memo The transaction memo
       * @param feeLimit The energy fee limit
       * @return A TransactionReturn object contains the trigger result(true / false)
       */
      public TransactionReturn transferFrom(String fromAddr, String destAddr, long amount, 
             String callerAddr, String cntrAddr, String memo, long feeLimit) throws Exception {
        Function transferFrom = new Function("transferFrom",
                Arrays.asList(new Address(fromAddr) ,new Address(destAddr),
                        new Uint256(BigInteger.valueOf(amount).multiply(BigInteger.valueOf(10).pow(18)))),
                Arrays.asList(new TypeReference<Bool>() {}));

        TransactionBuilder builder = client.triggerCall(callerAddr, cntrAddr, transferFrom);
        builder.setFeeLimit(feeLimit);
        builder.setMemo(memo);

        Transaction signedTxn = client.signTransaction(builder.build());
        return client.broadcastTransaction(signedTxn);
      }

      /**
       * Call function approve(address _spender, uint256 _value) public returns (bool success)
       * 
       * Allows _spender to withdraw from your account multiple times, up to the _value amount. 
       * If this function is called again it overwrites the current allowance with _value.
       * 
       * @param spender The address who is allowed to withdraw.
       * @param amount The amount allowed to withdraw.
       * @param callerAddr The caller's address
       * @param cntrAddr The contract's address
       * @param memo The transaction memo
       * @param feeLimit The energy fee limit
       * @return A TransactionReturn object contains the trigger result(true / false)
       */
      public TransactionReturn approve(String spender ,long amount, 
             String callerAddr, String cntrAddr, String memo, long feeLimit) throws Exception {
        Function approve = new Function("approve",
                Arrays.asList(new Address(spender) ,
                        new Uint256(BigInteger.valueOf(amount).multiply(BigInteger.valueOf(10).pow(18)))),
                Arrays.asList(new TypeReference<Bool>() {}));

        TransactionBuilder builder = client.triggerCall(callerAddr, cntrAddr, approve);
        builder.setFeeLimit(feeLimit);
        builder.setMemo(memo);

        Transaction signedTxn = client.signTransaction(builder.build());
        return client.broadcastTransaction(signedTxn);
      }

      /**
       * Call function allowance(address _owner, address _spender) public view returns (uint256 remaining).
       * 
       * Returns the amount which _spender is still allowed to withdraw from _owner.
       * 
       * @param owner The address to be withdrew from.
       * @param spender The address of the withdrawer.
       * @param callerAddr The caller's address
       * @param cntrAddr The contract's address
       * @return A TransactionExtention object contains constant result to resolve
       */
      public BigInteger getAllowance(String owner, String spender,
             String callerAddr, String cntrAddr) throws Exception {
        Function allowance = new Function("allowance",
                Arrays.asList(new Address(owner), new Address(spender)),
                Arrays.asList(new TypeReference<Uint256>() {}));
        
        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, allowance);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, allowance.getOutputParameters()).get(0).getValue();
      }
 }