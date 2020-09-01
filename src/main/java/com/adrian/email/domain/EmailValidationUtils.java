package com.adrian.email.domain;

import com.adrian.email.api.EmailRequest;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;

@UtilityClass
public class EmailValidationUtils {

    public static Validation<Seq<String>, ValidEmail> validateEmail(EmailRequest email) {
        return Validation.combine(
                validateEmail(email.getToEmail()),
                validateName(email.getToName()),
                validateEmail(email.getFromEmail()),
                validateName(email.getFromName()),
                validateSubject(email.getSubject()),
                validateBody(email.getBody())
            ).ap(ValidEmail::new);
    }

    static Validation<String, String> validateEmail(String email) {
        final String EMAIL_PATTERN = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return validStringByPattern("email", email, EMAIL_PATTERN);
    }

    public static Validation<String, String> validateSnailgunID(String snailgunId) {
        final String SNAILGUN_ID_PATTERN = "snailgun_email_[a-zA-Z0-9+_.-]*";
        return validStringByPattern("snailgunId", snailgunId, SNAILGUN_ID_PATTERN);
    }

    // TODO update validation of email body to return an escaped body
    static Validation<String,String> validateBody(String body) {
        final String BODY_ERROR = "Given email body [ " + body + "] can not be empty.";
        return isNotEmptyString(body, BODY_ERROR);
    }

    static Validation<String,String> validateName(String name) {
        final String NAME_ERROR = "Given name [ " + name + "] can not be empty.";
        return isNotEmptyString(name, NAME_ERROR);
    }

    static Validation<String,String> validateSubject(String subject) {
        final String SUBJECT_ERROR = "Given subject line [ " + subject + " ] can not be empty.";
        return isNotEmptyString(subject, SUBJECT_ERROR);
    }

     static Validation<String, String> validStringByPattern(String field, String input, String pattern) {
        final String errorMessage = "Given input [ " + input + " ] does not match the "+ field + " standard Pattern.";
        return StringUtils.isNotEmpty(input) && input.matches(pattern) ? valid(input) : invalid(errorMessage);
    }

    static <E, String> Validation<E, String> isNotEmptyString(String t, E e) {
        return StringUtils.isBlank((CharSequence) t) ? invalid(e) : valid(t);
    }
}
