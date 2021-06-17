package it.studioform.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.studioform.test.dto.NodeDto;
import it.studioform.test.dto.NodeInsertDto;
import it.studioform.test.dto.NodeUpdateDto;
import it.studioform.test.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/node")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @Operation(description = "Find a simple node by its id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Node not found")
    })
    @GetMapping("/{id}")
    public NodeDto findById(@PathVariable @NotNull Integer id) {
        return nodeService.findById(id);
    }

    @Operation(description = "Create a new node" , method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Parent node not found"),
            @ApiResponse(responseCode = "400", description = "At least a node attribute is not correct")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public NodeDto createNode(@Validated(NodeDto.NewNodeValidationGroup.class)
                                  @RequestBody NodeInsertDto nodeDto) {
        return nodeService.saveNode(nodeDto);
    }

    @Operation(description = "Update a node" , method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Parent node or node not found"),
            @ApiResponse(responseCode = "400", description = "At least a node attribute is not correct")
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public NodeDto update(@Validated(NodeDto.UpdateNodeValidationGroup.class)
                              @RequestBody NodeUpdateDto nodeDto) {
        return nodeService.updateNode(nodeDto);
    }

    @Operation(description = "Retrieve the whole hierarchy for a given node" , method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Node not found"),
    })
    @GetMapping(value = "/tree/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public NodeDto findTreeByNode(@PathVariable @NotNull Integer id) {
        return nodeService.findTreeByNode(id);
    }
}
