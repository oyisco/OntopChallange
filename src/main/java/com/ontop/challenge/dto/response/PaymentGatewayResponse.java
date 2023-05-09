package com.ontop.challenge.dto.response;

import lombok.Data;

@Data
public class PaymentGatewayResponse {
    private RequestInfo requestInfo;
    private PaymentInfo paymentInfo;
}
