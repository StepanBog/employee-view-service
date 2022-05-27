package ru.bogdanov.diplom.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class PhoneUtils {

    public String addPrefix(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return "";
        }
        return StringUtils.prependIfMissing(phone,"+7");
    }
}
