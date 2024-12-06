package recordshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @RequestMapping("/**")
    public ResponseEntity<String> handleUnavailableRoutes() {
        return new ResponseEntity<>("The requested endpoint was not found", HttpStatus.NOT_FOUND);
    }
}
