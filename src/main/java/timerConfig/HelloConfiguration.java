package timerConfig;

import java.util.function.Function;

import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.util.InetAddressPredicates;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.cors.CorsService;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;

/**
 * An example of a configuration which provides beans for customizing the server and client.
 */
@Configuration
public class HelloConfiguration {

    /**
     * A user can configure a {@link Server} by providing an {@link ArmeriaServerConfigurator} bean.
     */
    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(HelloAnnotatedService service) {
        Function<? super HttpService, CorsService> corsService =
                CorsService.builderForAnyOrigin()
                           .allowCredentials()
                           .allowRequestMethods(HttpMethod.POST, HttpMethod.GET)
                           .allowRequestHeaders(HttpHeaders.CONTENT_TYPE,
                                                HttpHeaders.AUTHORIZATION)
                           .exposeHeaders("expose_header_1", "expose_header_2")
                           .preflightResponseHeader("x-preflight-cors", "Hello CORS")
                           .newDecorator();
        // Customize the server using the given ServerBuilder. For example:
        return builder -> {
            builder.clientAddressTrustedProxyFilter(InetAddressPredicates.ofCidr("0.0.0.0/0"));
            // Add DocService that enables you to send Thrift and gRPC requests from web browser.
            builder.serviceUnder("/docs", new DocService());

            // Log every message which the server receives and responds.
            builder.decorator(LoggingService.newDecorator());

            // Write access log after completing a request.
            builder.accessLogWriter(AccessLogWriter.combined(), false);

            // Add an Armeria annotated HTTP service.
            builder.annotatedService(service, corsService);

            // You can also bind asynchronous RPC services such as Thrift and gRPC:
            // builder.service(THttpService.of(...));
            // builder.service(GrpcService.builder()...build());
        };
    }
}
