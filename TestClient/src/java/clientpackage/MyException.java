package clientpackage;

/**
 *
 * @author AezA
 */
public class MyException extends Exception {
    
    private String explanation;
    
    public MyException(String message, String explanation, Throwable cause) {
        super(message, cause);
        this.explanation = explanation;
    }      

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }        
    
}
