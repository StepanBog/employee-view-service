package ru.bogdanov.diplom.backend.mapper.common;

import com.google.protobuf.StringValue;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

@Mapper
public interface StringValueMapper {

    default String mapToJava(StringValue value) {
        if (value == null || StringUtils.isBlank(value.getValue())) {
            return null;
        }

        return value.getValue();
    }

    default StringValue mapToProto(String value) {
        if (StringUtils.isBlank(value)) {
            return StringValue.getDefaultInstance();
        }

        return StringValue.of(value);
    }
}
