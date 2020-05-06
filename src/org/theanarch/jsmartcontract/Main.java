package org.theanarch.jsmartcontract;

import org.json.JSONObject;
import org.theanarch.jsmartcontract.BlockChain.Block;
import org.theanarch.jsmartcontract.SmartContract.SmartContract;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import static org.theanarch.jsmartcontract.BlockChain.BlockHandler.isChainValid;
import static org.theanarch.jsmartcontract.BlockChain.Crypto.*;

public class Main {

    public static void main(String[] args){

        ArrayList<Block> blockChain = new ArrayList<>();

        //WHEN WE START A SMART CONTRACT WE HAVE 2 OPTIONS
        //  WE CAN CREATE AN ENCRYPTED ONE USING RSA - TO ENSURE ITS FROM SOME USER
        //  OR WE DON'T MAKE IT ENCRYPTED - NO WAY TO PROVE WHO MADE THE BLOCK
        // ETHER WAY THE SMART CONTRACT IS IMMUTABLE

        //IN THIS CASE WE WILL ENCRYPT THE SMART CONTRACT TO DEMONSTRATE METHOD 1

        try{
            //FIRST THING WE WANT TO DO IS CREATE THE KEY-PAIR - THIS WILL CREATE AN RSA PUBLIC & PRIVATE KEY
            KeyPair keyPair = generateKeyPair();

            //AT THIS POINT WE WOULD WANT TO MAKE THE FIRST BLOCK "GENESIS BLOCK" THE RSA PUBLIC-KEY
            //THIS MAKES IT SO THAT ANY UPDATES TO THE CHAIN CAN BE PROVEN WITH THAT KEY, TO PROVE LEGITIMACY
            //OF ANY NEW INPUT TO THE CHAIN

            //WE WILL BE USING JSON, HOWEVER XML MAY BE A BETTER OPTION
            JSONObject jgenesis = new JSONObject();
            jgenesis.put("o", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            jgenesis.put("n", hashString("NAME-OF-CHAIN"));

            //THE FIRST BLOCK IN ANY BLOCK CHAIN MUST BE 0
            Block genesis = new Block(jgenesis.toString(), "0");
            genesis.mineBlock(2);
            blockChain.add(genesis);



            //NOW THAT WE HAVE THE GENESIS CREATED LETS CREATE A SMART CONTRACT, IN THIS CASE WE WILL MAKE A CHAIN,
            //HOWEVER IF IN MOST CASES YOU WILL WANT TO MAKE THE SMART CONTRACT REFER TO THE GENESIS BLOCK BUT NOT
            //A PART OF THE CHAIN

            //THE SMART CONTRACT LANGUAGE IS IN JAVA-SCRIPT - THERE ARE SOME BIG THINGS TO THINK ABOUT WITH FORCING
            //MINERS TO RUN CODE, FOR EXAMPLE THE USER CAN POST A NEVER ENDING SCRIPT - WHICH INTERN FORCES THE MINER
            //TO WASTE CPU USAGE. HERE ARE SOME POSSIBLE WAYS TO COMBAT THIS
            // - DISABLE ALL LOOPS AND FUNCTIONS
            // - MAX SCRIPTS TO 10 SECOND RUNTIME
            // - FORCE USERS TO PAY FOR EVERY BIT OF CODE RUN
            // - FORCE USERS TO PAY BY TIME THAT IT TAKES TO RUN THE CODE

            //PAYMENT POSSIBILITIES
            // - CPU CREDIT SYSTEM
            // - STORAGE CREDIT SYSTEM
            // - REAL MONEY | CRYPTO CURRENCY


            //IN THIS CASE WE HAVE DECIDED TO MAX SCRIPTS AT 10 SECOND RUNTIME
            //WE ARE MINING LOCALLY, HOWEVER THIS WONT BE THE CASE FOR ANY REAL USE OF A SMART CONTRACT

            //ALSO NOTE THE SCRIPT DOES NOT ELAPSE TIME, AS THIS CAN CHANGE THE OUTPUT AND CANNOT BE PROVEN BY OTHER MINERS
            //RANDOM IS ALSO CHANGED TO BE SEEDED FROM THE TIMESTAMP

            String script = "" +
                    "for(var i = 0; i < 10; i++){\n" +
                    "    out.println('Looped: '+(i+varName));\n" +
                    "}\n" +
                    "var response = 'hello world';";

            Block smartContract = new Block(encrypt(script, keyPair.getPrivate()), genesis.hash);
            smartContract.mineBlock(2);
            blockChain.add(smartContract);

            //LETS CHECK THE VALIDITY OF THE CHAIN REAL-QUICK
            if(isChainValid(blockChain)){
                System.out.println("BlockChain is valid, lets continue.");

                //IN THE REQUEST WE MUST INCLUDE THE TIME IN WHICH WE ARE RUNNING
                JSONObject request = new JSONObject();
                request.put("time", new Date().getTime());
                request.put("varName", 20);

                (new SmartContract(request, blockChain)).start();

            }else{
                System.err.println("BlockChain has some errors...");
            }








        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
