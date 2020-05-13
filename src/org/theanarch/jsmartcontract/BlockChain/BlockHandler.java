package org.theanarch.jsmartcontract.BlockChain;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BlockHandler {

    public static int difficulty = 2;

    public static JSONObject blockToJSON(Block block){
        JSONObject jblock = new JSONObject();
        jblock.put("p", block.previousHash);
        jblock.put("d", block.data);
        jblock.put("t", block.timeStamp);
        jblock.put("n", block.nonce);
        jblock.put("h", block.hash);
        return jblock;
    }

    public static JSONArray blocksToJSON(ArrayList<Block> blocks){
        JSONArray jblocks = new JSONArray();
        for(Block block : blocks){
            JSONObject jblock = new JSONObject();
            jblock.put("p", block.previousHash);
            jblock.put("d", block.data);
            jblock.put("t", block.timeStamp);
            jblock.put("n", block.nonce);
            jblock.put("h", block.hash);
            jblocks.put(jblock);
        }

        return jblocks;
    }

    public static Block stringToBlock(String text){
        JSONObject json = new JSONObject(text);
        Block block = new Block(json.getString("d"),
                json.getString("p"),
                json.getString("h"),
                json.getLong("t"),
                json.getInt("n"));

        return block;
    }

    public static ArrayList<Block> stringToBlocks(String text){
        JSONArray json = new JSONArray(text);
        ArrayList<Block> blocks = new ArrayList<>();
        for(int i = 0; i < json.length(); i++){
            JSONObject jblock = json.getJSONObject(i);
            Block block = new Block(jblock.getString("d"),
                    jblock.getString("p"),
                    jblock.getString("h"),
                    jblock.getLong("t"),
                    jblock.getInt("n"));

            blocks.add(block);
        }
        return blocks;
    }

    public static Boolean isBlockValid(Block block){
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        
        //compare registered hash and calculated hash:
        if(!block.hash.equals(block.calculateBlockHash())){
            //System.out.println("Current Hashes not equal");
            return false;
        }
        //check if hash is solved
        Matcher matcher = Pattern.compile("(.*?)[a-f1-9].*").matcher(block.hash);
        if(matcher.matches()){
            if(!matcher.group(1).equals(hashTarget)){
                return false;
            }
        }
        return true;
    }

    public static Boolean isChainValid(ArrayList<Block> blocks){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for(int i = 1; i < blocks.size(); i++){
            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateBlockHash())){
                //System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash)){
                //System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            Matcher matcher = Pattern.compile("(.*?)[a-f1-9].*").matcher(currentBlock.hash);
            if(matcher.matches()){
                if(!matcher.group(1).equals(hashTarget)){
                    return false;
                }
            }
        }
        return true;
    }
}
