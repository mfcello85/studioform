package it.studioform.service;

import it.studioform.test.dto.NodeDto;
import it.studioform.test.dto.NodeInsertDto;
import it.studioform.test.dto.NodeUpdateDto;
import it.studioform.test.entity.Node;
import it.studioform.test.exceptions.NodeNotFoundException;
import it.studioform.test.mapper.NodeMapper;
import it.studioform.test.repository.NodeRepository;
import it.studioform.test.service.NodeService;
import it.studioform.util.CommonTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NodeServiceTest extends CommonTestUtil {


    private NodeService nodeService;
    private final NodeMapper mapper
            = Mappers.getMapper(NodeMapper.class);
    @Mock
    private NodeRepository nodeRepository;

    @Captor
    private ArgumentCaptor<Node> nodeCaptor;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        nodeService = new NodeService(nodeRepository, mapper);
    }

    @Test
    public void shouldReturnNodeIfFound(){
        Node found = getFound();
        when(nodeRepository.findById(1))
                .thenReturn(Optional.of(found));
        NodeDto node = nodeService.findById(1);
        assertEquals(node, mapper.mapToNode(found));

    }

    private Node getFound() {
        return getNode(1
                ,2
                ,"Description"
                , "name"
                , Collections.emptyList());
    }

    @Test
    public void shouldFailIfNotFound(){
         when(nodeRepository.findById(1))
                .thenReturn(Optional.empty());
        assertThrows(NodeNotFoundException.class
                ,() -> nodeService.findById(1));
    }

    @Test
    public void shouldSaveNodeWitExistingParent(){

        Node node = getFound();
        NodeInsertDto nodeInsertDto = new NodeInsertDto();
        nodeInsertDto.setParentId(1);
        nodeInsertDto.setDescription("Description");
        nodeInsertDto.setName("Name");

        Node savedNode = getNode(3,1
                , "Description", "Name", Collections.emptyList());
        when(nodeRepository.findById(1))
                .thenReturn(Optional.of(node));
        when(nodeRepository.save(mapper.mapToNewNode(nodeInsertDto)))
                .thenReturn(savedNode);
        NodeDto nodeDto = nodeService.saveNode(nodeInsertDto);

        verify(nodeRepository).findById(1);
        verify(nodeRepository).save(mapper.mapToNewNode(nodeInsertDto));

        assertEquals(mapper.mapToNode(savedNode), nodeDto);
     }

    @Test
    public void shouldFailIfParentNotFound(){

        NodeInsertDto nodeInsertDto = new NodeInsertDto();
        nodeInsertDto.setParentId(1);
        nodeInsertDto.setDescription("Description");
        nodeInsertDto.setName("Name");

        Node savedNode = getNode(3,1
                , "Description", "Name", Collections.emptyList());
        when(nodeRepository.findById(1))
                .thenReturn(Optional.empty());
        when(nodeRepository.save(mapper.mapToNewNode(nodeInsertDto)))
                .thenReturn(savedNode);
        assertThrows(NodeNotFoundException.class
                , ()-> nodeService.saveNode(nodeInsertDto));

        verify(nodeRepository).findById(1);
        verify(nodeRepository, Mockito.never()).save(mapper.mapToNewNode(nodeInsertDto));
    }

    @Test
    public void shouldFindByTree()
    {
        Node node = getFound();
        when(nodeRepository.findByIdRecursive(1))
                .thenReturn(Optional.of(node));
        NodeDto nodeDto = nodeService.findTreeByNode(1);
        verify(nodeRepository).findByIdRecursive(1);
        assertEquals(nodeDto , mapper.mapToNodeTree(node));
    }

    @Test
    public void shouldFailUpdateIfParentNotFound(){

        NodeUpdateDto nodeUpdateDto = new NodeUpdateDto();
        nodeUpdateDto.setParentId(2);
        nodeUpdateDto.setDescription("Description");
        nodeUpdateDto.setId(1);
        when(nodeRepository.findById(any()))
                .thenReturn(Optional.empty());
        NodeNotFoundException nodeNotFoundException = assertThrows(NodeNotFoundException.class
                , ()-> nodeService.updateNode(nodeUpdateDto));

        assertEquals(nodeNotFoundException.getId(),2);
        verify(nodeRepository).findById(any());
        verify(nodeRepository, Mockito.never()).save(any());
    }

    @Test
    public void shouldFailUpdateIfEntityNotFound(){

        NodeUpdateDto nodeUpdateDto = new NodeUpdateDto();
        nodeUpdateDto.setParentId(2);
        nodeUpdateDto.setDescription("Description");
        nodeUpdateDto.setId(1);
        when(nodeRepository.findById(2))
                .thenReturn(Optional.of(getFound()));
        NodeNotFoundException nodeNotFoundException = assertThrows(NodeNotFoundException.class
                , ()-> nodeService.updateNode(nodeUpdateDto));
        getNodeUpdateDto();
        assertEquals(nodeNotFoundException.getId(),1);

        verify(nodeRepository,times(2)).findById(any());
        verify(nodeRepository, Mockito.never()).save(any());
    }

    @Test
    public void shouldUpdate(){

        NodeUpdateDto nodeUpdateDto = getNodeUpdateDto();
        Node found = getFound();
        when(nodeRepository.findById(2))
                .thenReturn(Optional.of(found));
        when(nodeRepository.findById(1))
                .thenReturn(Optional.of(found));
        Node updatedNode = getNode(1,2, "Des"
                , "Nam", Collections.emptyList());
        when(nodeRepository.save(any()))
                .thenReturn(updatedNode);
        nodeService.updateNode(nodeUpdateDto);

        verify(nodeRepository).findById(2);
        verify(nodeRepository).findById(1);
        verify(nodeRepository).save(nodeCaptor.capture());

        Node savingNode = nodeCaptor.getValue();
        assertEquals(nodeUpdateDto.getDescription(), savingNode.getDescription());
        assertEquals(nodeUpdateDto.getParentId(), savingNode.getParentId());
    }

    private NodeUpdateDto getNodeUpdateDto() {
        NodeUpdateDto nodeUpdateDto = new NodeUpdateDto();
        nodeUpdateDto.setParentId(2);
        nodeUpdateDto.setDescription("Des");
        nodeUpdateDto.setId(1);
        return nodeUpdateDto;
    }

}
