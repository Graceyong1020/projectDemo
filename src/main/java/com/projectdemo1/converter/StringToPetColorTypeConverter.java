package com.projectdemo1.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.projectdemo1.domain.boardContent.color.PetColorType;

@Component
public class StringToPetColorTypeConverter implements Converter<String, PetColorType> {
    @Override
    public PetColorType convert(String source) {
        try {
            return PetColorType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // 필요 시 예외 처리 추가
        }
    }
}