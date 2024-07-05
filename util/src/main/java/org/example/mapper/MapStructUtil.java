package org.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapStructUtil {

    MapStructUtil INSTANCE = Mappers.getMapper(MapStructUtil.class);

    @Mapping(source = "username", target = "xm")
    UserVo copy(User user);
}
