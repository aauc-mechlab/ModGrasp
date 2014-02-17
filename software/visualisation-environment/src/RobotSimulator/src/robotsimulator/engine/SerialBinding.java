package robotsimulator.engine;

import gnu.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import se.krka.kahlua.vm.*;

/**
 * Lua bindings for the classes for serial communication
 * @author Stian Sandviknes
 */
public final class SerialBinding {
    private SerialBinding() {}
    
    /**
     * Create a new serial listener
     * @param engine
     * @return 
     */
    private static JavaFunction createSerialStringReader(final LuaEngine engine) {
        return new JavaFunction() {
            @Override
            public int call(LuaCallFrame lcf, int i) {
                if (lcf.get(0) instanceof String && lcf.get(1) instanceof Double && lcf.get(2) instanceof LuaClosure) {
                    try {
                        SerialCommunication reader = new SerialCommunication(SerialComType.STRING,(String) lcf.get(0), (int)(double)(Double)lcf.get(1), (LuaClosure) lcf.get(2),engine);
                        lcf.push(reader);
                    } catch (Exception e) {
                        lcf.pushNil();
                    }
                } else {
                    lcf.pushNil();
                }
                return 1;
            }
        };
    }
    private static JavaFunction createSerialBinaryReader(final LuaEngine engine) {
        return new JavaFunction() {

            @Override
            public int call(LuaCallFrame lcf, int i) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
    
    
    
    
    
    private static final JavaFunction closeCom = new JavaFunction() {

        @Override
        public int call(LuaCallFrame lcf, int i) {
            if (lcf.get(0) instanceof SerialCommunication) {
                ((SerialCommunication)lcf.get(0)).close();
            }
            return 0;
        }
    };
    
    private static final LuaTable comMetaTable;
    static {
        comMetaTable = new LuaTableImpl();
        comMetaTable.rawset("close", closeCom);
        comMetaTable.rawset("__tostring", LuaTools.toString);
        comMetaTable.rawset("__index", comMetaTable);
    }
    
    
    
    
    public static void register(LuaState state, LuaEngine engine) {
        LuaTable tab = new LuaTableImpl();
        tab.rawset("StringReader", createSerialStringReader(engine));
        tab.rawset("BinaryReader", createSerialBinaryReader(engine));
        state.getEnvironment().rawset("serial", tab);
        state.setUserdataMetatable(SerialCommunication.class, comMetaTable);
    }
    
    
    
    
    
    
    private static enum SerialComType {
        STRING,
        BINARY
    }
    private static class SerialCommunication {
        private LuaClosure callback;
        private final LuaEngine engine;
        private final SerialPort sPort;
        private SerialComType type;
        private BufferedReader reader;
        public SerialCommunication(SerialComType type, String port, int baudrate, LuaClosure callback, final LuaEngine state) throws Exception {
            this.engine=state;
            this.callback=callback;
            this.type=type;
            CommPortIdentifier id = CommPortIdentifier.getPortIdentifier(port);
            sPort = (SerialPort) id.open(this.getClass().getName(),2000);
            sPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            sPort.addEventListener(stringListener);
            sPort.notifyOnDataAvailable(true);
            reader = new BufferedReader(new InputStreamReader(sPort.getInputStream()));
        }
        
        private void close() {
            try {
                reader.close();
                synchronized (sPort) {
                    sPort.close();
                    sPort.removeEventListener();
                    sPort.notifyAll();
                }
            } catch (Exception e) {
                System.out.println("Could not close com port");
            }
        }
        
        private SerialPortEventListener stringListener = new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent spe) {
                if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    if (type == SerialComType.BINARY) {
                        
                    } else {
                        try {
                            if (reader.ready()) {
                                synchronized (sPort) {
                                    engine.scheduleLuaCall(callback, new String[]{reader.readLine()});
                                    sPort.notifyAll();
                                }
                            }
                        } catch (IOException e) {
                            
                        }
                    }
                }
            }
        };
    }
    
    
}
