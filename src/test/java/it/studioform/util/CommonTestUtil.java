package it.studioform.util;

import it.studioform.test.dto.NodeDto;
import it.studioform.test.dto.NodeInsertDto;
import it.studioform.test.dto.NodeUpdateDto;
import it.studioform.test.entity.Node;

import java.util.List;

public class CommonTestUtil {

    protected Node getNode(Integer id
            , Integer parentId
            , String description
            , String name
            , List<Node> children) {
        Node node = new Node();
        node.setId(id);
        node.setParentId(parentId);
        node.setDescription(description);
        node.setName(name);
        node.setList(children);
        return node;
    }

    protected NodeDto getNodeDto(Integer id
            , Integer parentId
            , String description
            , String name
            , List<NodeDto> children) {
        NodeDto node = new NodeDto();
        node.setId(id);
        node.setParentId(parentId);
        node.setDescription(description);
        node.setName(name);
        node.setList(children);
        return node;
    }

    protected NodeInsertDto getNodeInsertDto(Integer parentId
            , String description
            , String name) {
        NodeInsertDto node = new NodeInsertDto();
        node.setParentId(parentId);
        node.setDescription(description);
        node.setName(name);
        return node;
    }

    protected NodeUpdateDto getNodeUpdateDto(Integer id
            , Integer parentId
            , String description) {
        NodeUpdateDto node = new NodeUpdateDto();
        node.setId(id);
        node.setParentId(parentId);
        node.setDescription(description);
        return node;
    }

}
