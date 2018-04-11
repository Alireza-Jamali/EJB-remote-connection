package EJBpackage;

import javax.ejb.Stateless;
import messagepackage.MyInterface;

/**
 *
 * @author AezA
 */
@Stateless(mappedName = "msg")
public class TheMessage implements MyInterface {

    @Override
    public String message() {
        
        return "connection was made successfully!";
    }
}
