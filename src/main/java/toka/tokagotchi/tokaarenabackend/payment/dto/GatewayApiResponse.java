package toka.tokagotchi.tokaarenabackend.payment.dto;

import lombok.Data;
/**
 * GatewayApiResponse: componente del modulo `payment`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Data
public class GatewayApiResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private T data;
}

