package org.theanarch.jsmartcontract.SmartContract;

import org.mozilla.javascript.*;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class BlockDate extends ScriptableObject {

    private static Long time;

    @Override
    public String getClassName(){
        return "Date";
    }

    public BlockDate(){
    }

    @JSConstructor
    public static Scriptable BlockDate(Context cx, Object[] args, Function func, boolean inNewExpr){
        if(args.length == 0 || args[0] == Context.getUndefinedValue()){
            time = (Long) cx.getThreadLocal("time");
        }else{
            time = ((Number)args[0]).longValue();
        }

        return new BlockDate();
    }

    @JSFunction
    public Long getTime(){
        return time;
    }

    @JSFunction
    public String getFullYear(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("Y");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(time);
    }

    @JSFunction
    public String getMonth(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(time);
    }

    @JSFunction
    public String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(time);
    }

    @JSFunction
    public String getHours(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(time);
    }

    @JSFunction
    public String getMinutes(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("m");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(time);
    }

    @JSFunction
    public String getSeconds(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("s");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(time);
    }

    @JSFunction
    public String getMilliseconds(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("S");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(time);
    }

    @JSFunction
    public String getDay(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("u");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(time);
    }
}
