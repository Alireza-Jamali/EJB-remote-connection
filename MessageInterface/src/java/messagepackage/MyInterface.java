package messagepackage;

import javax.ejb.Remote;

/**
 *
 * @author AezA
 */
@Remote
public interface MyInterface {

    String message();
}
