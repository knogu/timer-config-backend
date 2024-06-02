package timerConfig;

import java.io.IOException;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.linecorp.armeria.server.annotation.ExceptionHandler;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.ProducesJson;

import jakarta.validation.constraints.Size;

/**
 * Note that this is not a Spring-based component but an annotated HTTP service that leverages
 * Armeria's built-in annotations.
 *
 * @see <a href="https://armeria.dev/docs/server-annotated-service">Annotated HTTP Service</a>
 */
@Component
@Validated
@ExceptionHandler(ValidationExceptionHandler.class)
public class HelloAnnotatedService {
    private ConfigDBService db;
    @Autowired
    public HelloAnnotatedService(ConfigDBService db) {
        this.db = db;
    }

    @Get("/")
    public String defaultHello() {
        return "Hello, world! Try sending a GET request to /hello/armeria";
    }

    /**
     * An example in order to show how to use validation framework in an annotated HTTP service.
     * @param name The name that should be greeted.
     */
    @Get("/hello/{name}")
    public String hello(
            @Size(min = 3, max = 10, message = "name should have between 3 and 10 characters")
            @Param String name) {
        return String.format("Hello, %s! This message is from Armeria annotated service!", name);
    }

    @Get("/config/{userId}")
    @ProducesJson
    public TimerConfig getConfig(@Param String userId) throws IOException {
        return db.get(userId);
    }
}
