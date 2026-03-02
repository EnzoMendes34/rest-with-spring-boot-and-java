package EnzoMendes.com.github.services;

import EnzoMendes.com.github.config.EmailConfig;
import EnzoMendes.com.github.data.dto.request.EmailRequestDTO;
import EnzoMendes.com.github.mail.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    private final EmailSender emailSender;
    private final EmailConfig emailConfig;

    public EmailService(EmailSender emailSender, EmailConfig emailConfig){
        this.emailConfig = emailConfig;
        this.emailSender = emailSender;
    }

    public void sendSimpleEmail(EmailRequestDTO dto){
        emailSender.To(dto.getTo()).withSubject(dto.getSubject()).withMessage(dto.getBody()).sendMail(emailConfig);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment){
        File tempFile = null;
        try {
            EmailRequestDTO emailRequestDTO = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailSender
                    .To(emailRequestDTO.getTo())
                    .withSubject(emailRequestDTO.getSubject())
                    .withMessage(emailRequestDTO.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .sendMail(emailConfig);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while parsing e-mail request", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment, try again.", e);
        } finally {
            if(tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }
}
