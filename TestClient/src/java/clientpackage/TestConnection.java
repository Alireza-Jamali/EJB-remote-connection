package clientpackage;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import messagepackage.MyInterface;

/**
 *
 * @author AezA
 */
public class TestConnection {

    private final String SESSION_BEAN_MAPPED_NAME;
    private final String SERVER_IP_ADDRESS;//change to server IP address

    public TestConnection(String radioBtn, String serverIp) {
        switch (radioBtn) {
            default:
                SESSION_BEAN_MAPPED_NAME = "msg";
                break;
            case "remote":
                SESSION_BEAN_MAPPED_NAME = "java:global/EJBConnectionTest/EJBConnectionTest-ejb/TheMessage!messagepackage.MyInterface";
//                SESSION_BEAN_MAPPED_NAME = "java:global/EJBConnectionTest/EJBConnectionTest-ejb/TheMessage";
//                SESSION_BEAN_MAPPED_NAME = "msg#messagepackage.MyInterface";
                break;
        }
        SERVER_IP_ADDRESS = serverIp == null || serverIp.isEmpty() ? "localhost" : serverIp;
    }

    String connect() throws NamingException, TimeoutException {

        Properties props = new Properties();
        try {
            props.put("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
            props.put("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
            props.put("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.put("org.omg.CORBA.ORBInitialHost", SERVER_IP_ADDRESS);
            props.put("org.omg.CORBA.ORBInitialPort", "3700");
            
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "props value is null";
        }

        Context ctx;
        String msg = null;

        try {
            ctx = new InitialContext(props);
            MyInterface mi = lookupWithTimeup(ctx, 30);//time up
            if (mi == null) {
                throw exx;
            }
            msg = mi.message();
        } catch (TimeoutException ex) {
            throw new TimeoutException("time up!" + " connection failed to session bean: " + SESSION_BEAN_MAPPED_NAME + " ip: " + SERVER_IP_ADDRESS);
        } catch (NamingException ex) {
            throw ex;
        }
        return msg;

    }

    MyInterface mi;
    NamingException exx;

    private MyInterface lookupWithTimeup(Context ctx, int timeup) throws NamingException, TimeoutException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws NamingException {
                try {
                    mi = (MyInterface) ctx.lookup(SESSION_BEAN_MAPPED_NAME);
                } catch (NamingException ex) {
                    exx = ex;
                }
                return "ok";
            }
        };
        Future future = executor.submit(call);
        try {
            future.get(timeup, TimeUnit.SECONDS);

        } catch (TimeoutException ex) {
            throw new TimeoutException("time up");
        } catch (ExecutionException | InterruptedException ex) {
            ex.printStackTrace();
        }

        return mi;
    }

}
