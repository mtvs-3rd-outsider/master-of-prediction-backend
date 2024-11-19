package com.outsider.masterofpredictionbackend.betting.command.application.service;


import lombok.Getter;

@Getter
public enum BettingProductMessageCode {
    OPTIONS_IMAGE_REQUIRED("options.image.required"),
    OPTIONS_CONTENT_REQUIRED("options.content.required"),
    CREATE_SUCCESS("betting.create.success"),
    CREATE_FAILED("betting.create.fail"),
    TIME_FORMAT_INVALID("error.time.format.invalid"),
    PRODUCT_OPTION_MISSING("error.product.option.missing"),
    NOT_FOUND("betting.not.found");

    private final String code;

    BettingProductMessageCode(String code) {
        this.code = code;
    }

}

