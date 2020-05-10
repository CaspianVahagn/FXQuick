package sample.application.service;

import feign.Client;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import fxQuick.FXFeign.ErrorResponse;
import fxQuick.FXFeign.FXFeignClient;
import fxQuick.FXFeign.FXFeignConfig;
import fxQuick.annotations.FXConfiguration;
import fxQuick.annotations.FXService;
import fxQuick.exeptions.FXViewException;

import java.lang.reflect.Method;

@FXConfiguration
public class ConfigForFeign implements FXFeignConfig {

    private final OtherService otherService;

    public ConfigForFeign(OtherService otherService) {
        this.otherService = otherService;
    }

    @Override
    public Encoder encoder() {
        return new JacksonEncoder();
    }

    @Override
    public Decoder decoder() {
        return new JacksonDecoder();
    }

    @Override
    public Client client() {
        return new OkHttpClient();
    }

    @Override
    public RequestInterceptor requestInterceptor() {
        return template ->  otherService.test();
    }

    @Override
    public ErrorDecoder errorDecoder(){
        return (methodKey, response) -> new ErrorResponse("ERROR Feign client: " + methodKey , response);
    }
}
