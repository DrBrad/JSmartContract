package org.theanarch.jsmartcontract.BlockChain;

import java.security.MessageDigest;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Block {

    public String hash;
    public String previousHash;
    public String data;
    public long timeStamp;
    public int nonce;

    public Block(String data, String previousHash){
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateBlockHash();
    }

    public Block(String data, String previousHash, String hash, long timeStamp, int nonce){
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
        this.timeStamp = timeStamp;
        this.nonce = nonce;
    }

    public String calculateBlockHash(){
        String dataToHash = previousHash+timeStamp+nonce+data;

        byte[] bytes;
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(UTF_8));

            StringBuffer buffer = new StringBuffer();
            for(byte b : bytes){
                buffer.append(String.format("%02x", b));
            }

            return buffer.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String mineBlock(int difficulty){
        String prefixString = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring(0, difficulty).equals(prefixString)){
            nonce++;
            hash = calculateBlockHash();
        }
        System.out.println("Block Mined!!! : " + hash);
        return hash;
    }
}
