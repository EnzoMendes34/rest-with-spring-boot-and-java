package EnzoMendes.com.github.controllers;

import EnzoMendes.com.github.controllers.docs.EmailControllerDocs;
import EnzoMendes.com.github.data.dto.request.EmailRequestDTO;
import EnzoMendes.com.github.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs {

    private final EmailService service;

    public EmailController(EmailService service){
        this.service = service;
    }

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        service.sendSimpleEmail(emailRequestDTO);
        return new ResponseEntity<>("E-mail sent with success", HttpStatus.OK);
    }

    @PostMapping(value ="/withAttachment",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAtacchment(
            @RequestParam("emailRequest") String emailRequest,
            @RequestParam("attachment")MultipartFile attachment) {
        service.sendEmailWithAttachment(emailRequest, attachment);
        return new ResponseEntity<>("E-mail with attachment sent successfully", HttpStatus.OK);
    }
}
