package it.studioform.test.mapper;

import it.studioform.test.dto.NodeDto;
import it.studioform.test.dto.NodeInsertDto;
import it.studioform.test.entity.Node;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NodeMapper {

    @Mapping(target = "list", ignore = true)
    NodeDto mapToNode(Node node);

    @Mapping(target = "id", ignore = true)
    Node mapToNewNode(NodeInsertDto node);

    default NodeDto mapToNodeTree(Node node)
    {
        NodeDto dto = new NodeDto();
        dto.setId(node.getId());
        dto.setDescription(node.getDescription());
        dto.setName(node.getName());
        dto.setParentId(node.getParentId());
        dto.setList(Optional.ofNullable(node.getList())
                .orElseGet(ArrayList::new)
                .stream()
                .map(this::mapToNodeTree)
                .collect(Collectors.toList())
        );

        return dto;
    }
}
