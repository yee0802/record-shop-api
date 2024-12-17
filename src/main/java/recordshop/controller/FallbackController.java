package recordshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recordshop.exception.UnavailableRouteException;

@RestController
public class FallbackController {

    @RequestMapping("/**")
    public ResponseEntity<String> handleUnavailableRoutes() {
        throw new UnavailableRouteException("The requested endpoint was not found");
    }
}
