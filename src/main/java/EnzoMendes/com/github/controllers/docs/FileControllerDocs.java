package EnzoMendes.com.github.controllers.docs;

import EnzoMendes.com.github.data.dto.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "File Endpoint")
public interface FileControllerDocs {

    @Operation(
            summary = "Saves a file",
            description = "Recieves e Multipart file, that is saved by the service method and returns the file name, the download uri, file type and file size",
            tags = "File Endpoint",
            responses ={
                    @ApiResponse(
                            description = "Success, file uploaded",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UploadFileResponseDTO.class))
                    ),
            @ApiResponse(description = "No content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    UploadFileResponseDTO uploadFile(@RequestParam("file")MultipartFile file);

    @Operation(
            summary = "Saves multiple files",
            description = "Recieves a list of Multipart files and calls the method uploadFile for each file, returns a json list with the files names, the URIs, file types and the file size.",
            tags = "File Endpoint",
            responses ={
                    @ApiResponse(
                            description = "Success, files uploaded",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UploadFileResponseDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files);

    @Operation(
            summary = "Download multiple files",
            description = "Reads a file in the disk and try to determinate its content type and returns the content in body and the content type in header" +
            " you must pass a link in the request",
            tags = "File Endpoint",
            responses ={
                    @ApiResponse(
                            description = "Success, files downloaded",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UploadFileResponseDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request);
}
