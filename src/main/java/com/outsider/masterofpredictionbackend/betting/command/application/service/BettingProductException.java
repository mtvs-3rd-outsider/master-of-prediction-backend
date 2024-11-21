package com.outsider.masterofpredictionbackend.betting.command.application.service;

public class BettingProductException {

    public static class NotFound extends RuntimeException {
        public NotFound() {super();}
    }

    public static class BadRequest extends RuntimeException {
        public BadRequest() {super();}
    }

    public static class Unauthorized extends RuntimeException {
        public Unauthorized() {super();}
    }
}
