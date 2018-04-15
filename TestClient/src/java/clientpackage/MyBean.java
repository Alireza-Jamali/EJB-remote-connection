package clientpackage;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.NamingException;

/**
 *
 * @author AezA
 */
@ManagedBean
@RequestScoped
public class MyBean {
    
    String msg;
    String err;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void click(String radioBtn, String ip) {

        TestConnection tlc = new TestConnection(radioBtn, ip);
        try {
            msg = tlc.connect();
        } catch (Exception e) {
            msg = "could not connect!";
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            if (e instanceof NamingException) {
                err = ((NamingException) e).getExplanation() + "\n\n";
            }
            err +=  "\n" + sw.toString();
        }        
    }
}
