package com.ontop.challenge.dto.request;

import lombok.Data;

@Data
public class APIRequest {
    public SourceRequest source;
    public DestinationRequest destination;
    public int amount;
}
