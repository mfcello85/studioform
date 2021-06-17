package it.studioform.mapper;

import it.studioform.test.dto.NodeDto;
import it.studioform.test.dto.NodeInsertDto;
import it.studioform.test.entity.Node;
import it.studioform.test.mapper.NodeMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NodeMapperTest {

    NodeMapper mapper = Mappers.getMapper(NodeMapper.class);

    @Test
    public void shouldMapNodeToSimpleNodeDto(){
        Node node = getNode(1,3, "Description", "nome", Arrays.asList(new Node(), new Node()));

        NodeDto nodeDto = mapper.mapToNode(node);
        assertEquals(node.getId(), nodeDto.getId());
        assertEquals(node.getName(), nodeDto.getName());
        assertEquals(node.getDescription(), nodeDto.getDescription());
        assertEquals(node.getParentId(), nodeDto.getParentId());
        assertNull(nodeDto.getList());
    }

    private Node getNode(Integer id
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

    @Test
    public void shouldMapToNewNode() {

        NodeInsertDto nodeDto = new NodeInsertDto();
        nodeDto.setId(1);
        nodeDto.setName("Name");
        nodeDto.setParentId(3);
        nodeDto.setDescription("Description");

        Node node = mapper.mapToNewNode(nodeDto);
        assertEquals(node.getName(), nodeDto.getName());
        assertEquals(node.getDescription(), nodeDto.getDescription());
        assertEquals(node.getParentId(), nodeDto.getParentId());
        assertNull(node.getId());
    }

    @Test
    public void shouldMapToNodeTree(){

        Node leaf31 = getNode(5, 3
                , "des31", "name32" , Lists.emptyList());
        Node leaf32 = getNode(6, 3
                , "des32", "name32", Lists.emptyList());
        Node leaf33 = getNode(7, 4
                , "des33", "name33", Lists.emptyList());

        Node node21 = getNode(3, 1
                , "des21", "name21" , Arrays.asList(leaf31, leaf32));
        Node node22 = getNode(4, 1
                , "des22", "name22" , Arrays.asList(leaf33));
        Node node1 = getNode(1, null
                , "des1", "name1" , Arrays.asList(node21, node22));
        NodeDto dto = mapper.mapToNodeTree(node1);
        basicEquals(dto, node1);
        List<NodeDto> firstLevel = dto.getList();
        NodeDto dto21 = firstLevel.get(0);
        basicEquals(dto21, node21);
        NodeDto dto22 = firstLevel.get(1);
        basicEquals(dto22, node22);

        List<NodeDto> secondLevel1 = dto21.getList();
        NodeDto dto31 = secondLevel1.get(0);
        basicEquals(dto31, leaf31);
        NodeDto nodeDto32 = secondLevel1.get(1);
        basicEquals(nodeDto32, leaf32);

        List<NodeDto> secondLevel2 = dto22.getList();
        NodeDto dto33 = secondLevel2.get(0);
        basicEquals(dto33, leaf33);
    }

    private void basicEquals(NodeDto nodeDto ,Node node)
    {
        assertEquals(node.getId(), nodeDto.getId());
        assertEquals(node.getName(), nodeDto.getName());
        assertEquals(node.getDescription(), nodeDto.getDescription());
        assertEquals(node.getParentId(), nodeDto.getParentId());
    }
}
