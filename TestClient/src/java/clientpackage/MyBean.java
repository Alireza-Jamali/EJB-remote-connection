package clientpackage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author AezA
 */
@ManagedBean
@SessionScoped
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

    public void click() {
    
        TestLocalConnection tlc = new TestLocalConnection();
        try {
            msg = tlc.connect();
        } catch (MyException e) {
            msg = e.getMessage();
            err = e.getExplanation();
        }        
    }
}
