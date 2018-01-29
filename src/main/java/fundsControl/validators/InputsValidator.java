package fundsControl.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputsValidator {
    private Pattern passwordPattern;
    private Pattern emailPattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public InputsValidator(){
        passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        emailPattern = Pattern.compile(EMAIL_PATTERN);
    }


    public boolean validatePassword(final String password){
        matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

    public boolean validateEmail(final String email) {
        matcher = emailPattern.matcher(email);
        return matcher.matches();
    }
}
