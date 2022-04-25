package tech.inno.odp.backend.mapper.common;

import com.google.protobuf.StringValue;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

@Mapper
public interface PhoneMapper {
    default String mapToProto(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }

        return StringUtils.prependIfMissing(phone,"+7");
    }
}
