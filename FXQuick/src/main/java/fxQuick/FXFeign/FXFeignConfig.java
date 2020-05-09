package fxQuick.FXFeign;


import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;

public interface FXFeignConfig {
    public Encoder encoder();
    public  Decoder decoder();
    public default RequestInterceptor requestInterceptor(){return null;}
    public default ErrorDecoder errorDecoder(){return null;}
    public  Client client();
    public default Logger logger(){return null;}
    public default Iterable<RequestInterceptor> requestInterceptors(){return null;};
    public default Logger.Level logLevel(){return null;};
    public default ExceptionPropagationPolicy exceptionPropagationPolicy(){return null;}
    public default Contract contract(){return null;};
}
