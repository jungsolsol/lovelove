package webapp.lovelove.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImgValidator.class)
public @interface ImageValid {
    String message() default "이미지는 필수입니다";
    Class[] groups() default {};
    Class[] payload() default {};

}
