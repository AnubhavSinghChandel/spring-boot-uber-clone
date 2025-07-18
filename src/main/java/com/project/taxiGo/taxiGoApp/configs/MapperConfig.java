package com.project.taxiGo.taxiGoApp.configs;

import com.project.taxiGo.taxiGoApp.dto.PointDTO;
import com.project.taxiGo.taxiGoApp.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(PointDTO.class, Point.class).setConverter(context -> {
            PointDTO pointDto = context.getSource();
            return GeometryUtil.createPoint(pointDto);
        });

        mapper.typeMap(Point.class, PointDTO.class).setConverter(context -> {
            Point point = context.getSource();
            double coordinates[] = {
                    point.getX(),
                    point.getY()
            };
            return new PointDTO(coordinates);
        });


        return mapper;
    }

}
