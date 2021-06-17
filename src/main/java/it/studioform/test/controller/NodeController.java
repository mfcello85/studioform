package it.studioform.test.controller;

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

    @GetMapping("/{id}")
    public NodeDto findById(@PathVariable @NotNull Integer id) {
        return nodeService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public NodeDto createNode(@Validated(NodeDto.NewNodeValidationGroup.class)
                                  @RequestBody NodeInsertDto nodeDto) {
        return nodeService.saveNode(nodeDto);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public NodeDto update(@Validated(NodeDto.UpdateNodeValidationGroup.class)
                              @RequestBody NodeUpdateDto nodeDto) {
        return nodeService.updateNode(nodeDto);
    }

    @GetMapping(value = "/tree/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public NodeDto findTreeByNode(@PathVariable @NotNull Integer id) {
        return nodeService.findTreeByNode(id);
    }
}
