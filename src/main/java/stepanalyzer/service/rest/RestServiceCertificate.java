package stepanalyzer.service.rest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stepanalyzer.bean.StepBean;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController @RequestMapping("/.well-known") public interface RestServiceCertificate {

    @GetMapping("/pki-validation/A81AD67E9C1154F809C1758388BADA7A.txt") ResponseEntity<Resource> certificateDownload() throws IOException;
}
