package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Data;

@Data
public class GatewayApiResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private T data;
}

