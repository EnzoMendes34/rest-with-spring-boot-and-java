package EnzoMendes.com.github.controllers.docs;

import EnzoMendes.com.github.data.dto.request.EmailRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface EmailControllerDocs {

    @Operation(
            summary = "Send an e-mail",
            description = "Sends an e-mail by providing details, subject and body",
            tags = "Email",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<String> sendEmail(EmailRequestDTO emailRequestDTO);

    @Operation(
            summary = "Send an e-mail with atacchment",
            description = "Sends an e-mail with atacchment by providing details, subject and body",
            tags = "Email",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<String> sendEmailWithAtacchment(String emailRequestJson, MultipartFile file);

}
