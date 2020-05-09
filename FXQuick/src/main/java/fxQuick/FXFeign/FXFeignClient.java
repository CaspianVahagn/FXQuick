package fxQuick.FXFeign;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FXFeignClient {
    /**
     *
     *
     */
    Class config() default FXFeignClient.class;

    String baseUrl();

    String api();
}
