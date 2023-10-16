package com.CVP.cv_project.dtos.utils;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.data.mapping.MappingException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EmptyStackException;
import java.util.List;
import java.util.stream.Collectors;

public class MappingUtils {

    public <T extends DTO> ModelMapper getMapper(Class<T> target){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setPropertyCondition(context ->
                        !(context.getSource() instanceof PersistentCollection)
                );

        return updateMapping(modelMapper, target);
    }

    public  <T extends DTO> ModelMapper updateMapping(ModelMapper mapper, Class<T> dto){
        try {
            Constructor<T> constructor = dto.getConstructor();
            T instance = constructor.newInstance();
            return instance.updateModelMapper(mapper, this);
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                | InvocationTargetException e){
            throw new MappingException(dto.getName());
        }
    }
    public  <S, T extends DTO> List<T> mapList(List<S> source, Class<T> target){
        ModelMapper modelMapper = getMapper(target);

        return source
                .stream().map(el -> modelMapper.map(el, target))
                .collect(Collectors.toList());
    }



    public <S extends DTO, T> List<T> mapListFromDTO(List<S> source, Class<T> target) throws EmptyStackException {
        if(source.isEmpty()){
            throw new EmptyStackException();
        }else {
            ModelMapper modelMapper = getMapper(source.get(0).getClass());

            return source
                    .stream().map(el -> modelMapper.map(el, target))
                    .collect(Collectors.toList());
        }
    }
    public <S extends DTO, T> T mapFromDTO(S source, Class<T> target){
        ModelMapper modelMapper = getMapper(source.getClass());

        return modelMapper.map(source, target);
    }
    public <S, T extends DTO> T map(S source, Class<T> target){
        ModelMapper modelMapper = getMapper(target);

        return modelMapper.map(source, target);
    }

    /*public  T mapToDTO(S source, Class<T> target){
        ModelMapper modelMapper = getMapper(target);

        return modelMapper.map(source, target);
    }*/

}
