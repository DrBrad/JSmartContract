package org.theanarch.jsmartcontract.SmartContract;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;
import org.mozilla.javascript.debug.Debugger;

import java.util.Date;

public class ObservingDebugger implements Debugger {

    private DebugFrame debugFrame;

    public DebugFrame getFrame(Context cx, DebuggableScript fnOrScript){
        if(debugFrame == null){
            debugFrame = new ObservingDebugFrame(new Date().getTime());
        }
        return debugFrame;
    }

    @Override
    public void handleCompilationDone(Context arg0, DebuggableScript arg1, String arg2){
    }

    private class ObservingDebugFrame implements DebugFrame {

        private long time;

        public ObservingDebugFrame(Long time){
            this.time = time;
        }

        public void onEnter(Context cx, Scriptable activation, Scriptable thisObj, Object[] args){
        }

        public void onLineChange(Context cx, int lineNumber){
            if(time+10000 <= new Date().getTime()){
                System.err.println("KILLED");
                throw new RuntimeException("Script Execution terminated");
            }
        }

        public void onExceptionThrown(Context cx, Throwable ex){
        }

        public void onExit(Context cx, boolean byThrow, Object resultOrException){
        }

        @Override
        public void onDebuggerStatement(Context arg0){
        }
    }
}



