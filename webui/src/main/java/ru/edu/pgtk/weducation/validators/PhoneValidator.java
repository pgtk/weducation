package ru.edu.pgtk.weducation.validators;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@FacesValidator("phoneValidator")
@Named("phoneValidator")
@Stateless
public class PhoneValidator implements Validator {

	private static final String PATTERN = "\\+7\\(\\d{3}\\)\\d{3}\\-\\d{2}\\-\\d{2} ?\\(?\\D*\\)?";

	private final Pattern pattern;

	public PhoneValidator() {
		pattern = Pattern.compile(PATTERN);
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String text = value.toString();
		if ((text != null) && !text.isEmpty()) {
			// Пробуем разобрать строку на составляющие и выполнить валидацию каждой составляющей
			for (String part : text.split(",")) {
				Matcher matcher = pattern.matcher(part.trim());
				if (!matcher.matches()) {
					FacesMessage msg
							= new FacesMessage("Некорректное значение телефонного номера: " + part, "Ошибка валидации.");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}
			}
		}
	}
}
