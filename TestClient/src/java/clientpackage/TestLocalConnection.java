package clientpackage;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import messagepackage.MyInterface;

/**
 *
 * @author AezA
 */
public class TestLocalConnection {

    private final String SESSION_BEAN_MAPPED_NAME = "msg";
    private final String SERVER_IP_ADDRESS = "localhost";//change to server IP address

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
            MyInterface mi = lookupWithTimeup(ctx, 5);
            msg = mi.message();
        } catch (TimeoutException ex) {
            throw new TimeoutException("time up!" + " connection failed to session bean: " + SESSION_BEAN_MAPPED_NAME + " ip: " + SERVER_IP_ADDRESS );
        } catch (NamingException ex) {
            throw new NamingException("could not connect!");
        }
        return msg;

    }

    MyInterface mi;

    private MyInterface lookupWithTimeup(Context ctx, int timeup) throws NamingException, TimeoutException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws Exception {
                mi = (MyInterface) ctx.lookup(SESSION_BEAN_MAPPED_NAME);
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
