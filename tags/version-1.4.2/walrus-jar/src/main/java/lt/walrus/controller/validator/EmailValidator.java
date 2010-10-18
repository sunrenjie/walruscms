package lt.walrus.controller.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	private final static String EMAIL_VALID_CHARS = "-!#$%&'*+.0-9=?A-Z^_`a-z{|}~";
	private final static String EMAIL_PATTERN = "^[" + EMAIL_VALID_CHARS + "]+@[" + EMAIL_VALID_CHARS + "]+[.][" + EMAIL_VALID_CHARS + "]+$" ;
	private final static Pattern PATTERN_EMAIL = Pattern.compile(EMAIL_PATTERN);
	
	public static boolean isValid(String email){
	    Matcher m = PATTERN_EMAIL.matcher(email);
		return m.matches();
	}
}
