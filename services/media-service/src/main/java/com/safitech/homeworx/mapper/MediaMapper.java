package com.safitech.homeworx.mapper;

import com.safitech.homeworx.dto.RespMedia;
import com.safitech.homeworx.entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MediaMapper {
    MediaMapper INSTANCE = Mappers.getMapper(MediaMapper.class);

    RespMedia toRespMedia(Media media);
}
