package com.green.acamatch.academy.model.JW;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusinessApiNumber {
    private boolean businessNumCheck;

    public boolean isvalid() {
        return businessNumCheck;
    }
}
