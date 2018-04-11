package clientpackage;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import messagepackage.MyInterface;

/**
 *
 * @author AezA
 */
public class TestLocalConnection {
    
    String err = "";    

    String connect() throws MyException {
        
        Properties props = new Properties();
        try {
            props.put("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
            props.put("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
            props.put("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.put("org.omg.CORBA.ORBInitialHost", "localhost");//destination ip address
            props.put("org.omg.CORBA.ORBInitialPort", "3700");
        } catch (Exception e) {
            e.printStackTrace();
            return "Could not connect!";
        }

        
        Context ctx;
        String s = null;
        
        try {
            ctx = new InitialContext(props);
            MyInterface mi = (MyInterface) ctx.lookup("msg");
            s = mi.message();
        } catch (NamingException ex) {
           throw new MyException("failed to lookup, check glassfish log", ex.getExplanation(), ex);           
        }
        return s;
        
    }
    
}
