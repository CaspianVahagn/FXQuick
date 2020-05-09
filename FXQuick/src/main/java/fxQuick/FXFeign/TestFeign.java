package fxQuick.FXFeign;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import sample.application.service.TestFeignFX;

public class TestFeign {

    public static final TestFeignFX client;
    static {
        client = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(TestFeignFX.class, "http://localhost:8081/test");
    }

}
