package fundsControl.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputsValidator {
    private static Matcher matcher;

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{6,20})";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//    private static final String AMOUNT_PATTERN;

    public InputsValidator(){
    }


    public static boolean validatePassword(final String password){
        Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

    public static boolean validateEmail(final String email) {
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

//    public static boolean validateAmount(final String amount) {
//        Pattern amountPattern;
//    }
}
