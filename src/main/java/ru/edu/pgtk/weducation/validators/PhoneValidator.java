package ru.edu.pgtk.weducation.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("phoneValidator")
public class PhoneValidator implements Validator {

  private static final String EMAIL_PATTERN = "\\+7\\(\\d{3}\\)\\d{3}\\-\\d{2}\\-\\d{2} ?\\(?\\D*\\)?";

  private final Pattern pattern;
  private Matcher matcher;

  public PhoneValidator() {
    pattern = Pattern.compile(EMAIL_PATTERN);
  }

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

    String text = value.toString();
    if ((text != null) && !text.isEmpty()) {
      // Пробуем разобрать строку на составляющие и выполнить валидацию каждой составляющей
      for (String part : text.split(",")) {
        matcher = pattern.matcher(part.trim());
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
