package com.tizo.br.mapper;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class ModelMapperUtil {

    private static final ModelMapper mapper = new ModelMapper();

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        List<D> list = new ArrayList<>();

        for (O o : origin) {
            list.add(mapper.map(o, destination));
        }
        return list;
    }
}
