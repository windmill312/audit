package com.windmill312.audit.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@ApiModel(description = "При ошибках возвращается данная структура")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse extends StatusfulResponse {

    @ApiModelProperty(value = "Описание ошибки")
    @JsonProperty("message")
    private String message;
}