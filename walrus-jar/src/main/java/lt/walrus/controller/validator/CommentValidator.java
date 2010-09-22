package lt.walrus.controller.validator;

import lt.walrus.model.Comment;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CommentValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return Comment.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "comment.error.empty.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "comment.error.empty.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "body", "comment.error.empty.body");
		String email = ((Comment)target).getEmail();
		if(StringUtils.hasText(email) && !EmailValidator.isValid(email) ){
			errors.rejectValue("email", "lt.walrus.error.invalidEmail");
		}
	
	}

}
