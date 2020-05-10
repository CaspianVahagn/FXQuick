package fxQuick.FXFeign;

import feign.Response;

public class ErrorResponse extends RuntimeException {

    private Response response;

    public ErrorResponse(String message, Response response) {
        super(message + ";\ncode:\t" + response.status() +"\nMethod:\t" + response.request().httpMethod() + "\nurl:\t" + response.request().url());
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
