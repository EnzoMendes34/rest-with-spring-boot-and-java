package EnzoMendes.com.github.controllers.docs;

import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.file.exporter.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PersonControllerDocs {

    @Operation(
            summary = "Find all People",
            description = "Finds all people in database",
            tags = "People",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0")Integer page,
            @RequestParam(value= "size", defaultValue = "12") Integer size,
            @RequestParam(value= "direction", defaultValue = "asc") String direction
    );

    @Operation(
            summary = "Creates more than one Person",
            description = "Saves more than one person by reading a .CSV or a .XLSX file",
            tags = "People",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = PersonDTO.class)
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    List<PersonDTO> createMultiplePeople(MultipartFile file);

    @Operation(
            summary = "Export people",
            description = "Export a page of people in .Csv or .Xlsx format",
            tags = "People",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaTypes.APPLICATION_XLSX_VALUE),
                                    @Content(mediaType = MediaTypes.APPLICATION_CSV_VALUE)
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0")Integer page,
            @RequestParam(value= "size", defaultValue = "12") Integer size,
            @RequestParam(value= "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    );

    @Operation(
            summary = "Find By first name",
            description = "Finds all people with the given first name",
            tags = "People",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPeopleByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0")Integer page,
            @RequestParam(value= "size", defaultValue = "12") Integer size,
            @RequestParam(value= "direction", defaultValue = "asc") String direction
    );

    @Operation(
            summary = "Find a Person",
            description = "Finds a specific person by your id",
            tags = "People",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    PersonDTO findById(@PathVariable("id") Long id);

    @Operation(
            summary = "Export Person's data",
            description = "Export a page of a specific person in .PDF format",
            tags = "People",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = MediaTypes.APPLICATION_PDF_VALUE)),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Resource> exportPerson(@PathVariable("id") Long id, HttpServletRequest request);

    @Operation(
            summary = "Adds a new Person",
            description = "Adds a new Person by passing a JSON, XML or YAML representation of the Person",
            tags = "People",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    PersonDTO create(@RequestBody PersonDTO person);

    @Operation(
            summary = "Updates a Person's information",
            description = "Updates a Person by passing a JSON, XML or YAML representation of the Person",
            tags = "People",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    PersonDTO update(@RequestBody PersonDTO person);

    @Operation(
            summary = "Disable a Person",
            description = "Disable a specific person by your id",
            tags = "People",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    PersonDTO disablePerson(@PathVariable("id") Long id);

    @Operation(
            summary = "Deletes a Person",
            description = "Deletes a specific Person by passing their ID",
            tags = "People",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "204",
                            content = @Content),
                    @ApiResponse(description = "No content", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<?> delete(@PathVariable("id") Long id);
}
