package tech.inno.odp.backend.mapper.common;

import com.google.protobuf.BoolValue;
import org.mapstruct.Mapper;

@Mapper
public interface BoolValueMapper {

    BoolValue mapToProto(Boolean value);

    default Boolean mapToJava(BoolValue value) {
        if (value == null) {
            return null;
        }
        return value.getValue();
    }
}
