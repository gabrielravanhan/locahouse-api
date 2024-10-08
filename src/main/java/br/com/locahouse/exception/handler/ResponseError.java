package br.com.locahouse.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
class ResponseError {

    private LocalDateTime timestamp;

    private String status;

    private int statusCode;

    private List<String> messages;
}
