package org.theanarch.jsmartcontract.SmartContract;

import org.json.JSONObject;
import org.mozilla.javascript.*;
import org.theanarch.jsmartcontract.BlockChain.Block;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import static org.theanarch.jsmartcontract.BlockChain.Crypto.decrypt;

public class SmartContract extends Thread {

    private ObservingDebugger observingDebugger;
    private JSONObject request;
    private String script;

    public SmartContract(JSONObject request, ArrayList<Block> blockChain){
        this.request = request;

        try{
            //WE HAVE TO PULL THE PUBLIC-KEY FROM THE FIRST BLOCK
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(
                    Base64.getDecoder().decode(new JSONObject(blockChain.get(0).data).getString("o").getBytes())));

            script = decrypt(blockChain.get(1).data, publicKey);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        Context context = Context.enter();
        observingDebugger = new ObservingDebugger();
        context.setDebugger(observingDebugger, new Integer(0));
        context.setGeneratingDebug(true);
        context.setOptimizationLevel(-1);
        context.setWrapFactory(new SandboxWrapFactory());

        context.setClassShutter(new ClassShutter(){
            @Override
            public boolean visibleToScripts(String s){
                if(s.equals("org.theanarch.jsmartcontract.SmartContract.BlockMath") ||
                        s.equals("java.lang.Long") ||
                        s.equals("java.io.PrintStream")){
                    return true;
                }
                //System.out.println(s);
                return false;
            }
        });

        try{
            long time = request.getLong("time");
            script = "var varName = "+request.getInt("varName")+";\n"+script;

            Scriptable scope = context.initStandardObjects();
            context.putThreadLocal("time", time);

            ScriptableObject.defineClass(scope, BlockDate.class);

            Object print = Context.javaToJS(System.out, scope);
            ScriptableObject.putProperty(scope, "out", print);

            Object random = Context.javaToJS(new BlockMath(time), scope);
            ScriptableObject.putProperty(scope, "Math", random);

            context.evaluateString(scope, script, "<cmd>", 1, null);

            System.out.println("SCRIPT - ENDED WITH: "+scope.get("response", scope));

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            Context.exit();
        }
    }
}
