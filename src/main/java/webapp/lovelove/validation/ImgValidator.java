package webapp.lovelove.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ImgValidator implements ConstraintValidator<ImageValid, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }
        return true;
    }
}


