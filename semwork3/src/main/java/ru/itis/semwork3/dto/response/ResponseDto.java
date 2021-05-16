package ru.itis.semwork3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseDto<T> {
    int type;
    T payload;
}
