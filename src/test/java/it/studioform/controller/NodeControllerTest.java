package it.studioform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.studioform.test.TestApplication;
import it.studioform.test.dto.NodeDto;
import it.studioform.test.dto.NodeInsertDto;
import it.studioform.test.dto.NodeUpdateDto;
import it.studioform.test.exceptions.NodeNotFoundException;
import it.studioform.test.service.NodeService;
import it.studioform.util.CommonTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
public class NodeControllerTest extends CommonTestUtil {

    @MockBean
    private NodeService nodeService;

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void findById() throws Exception {

        NodeDto nodeDto = getNodeDto(1,2, "Description"
                , "Name", Collections.emptyList());
        when(nodeService.findById(1))
                .thenReturn(nodeDto);
        mockMvc.perform(get("/node/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(nodeDto)));

    }

    @Test
    public void shouldFailToFindIdWhenIdNotFound() throws Exception {
        when(nodeService.findById(1))
                .thenThrow(new NodeNotFoundException(1));

        mockMvc.perform(get("/node/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateNode() throws Exception {

        NodeInsertDto nodeInsertDto = getNodeInsertDto(2, "Description"
                , "Name");
        NodeDto nodeDto = getNodeDto(1,2, "Description"
                , "Name", Collections.emptyList());

        when(nodeService.saveNode(nodeInsertDto))
                .thenReturn(nodeDto);
        mockMvc.perform(post("/node" )
                .content(mapper.writeValueAsString(nodeInsertDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(nodeDto)));
    }

    @Test
    public void shouldNotCreateGivenAMissingParentNode() throws Exception {

        NodeInsertDto nodeInsertDto = getNodeInsertDto(2, "Description"
                , "Name");

        when(nodeService.saveNode(nodeInsertDto))
                .thenThrow(new NodeNotFoundException(2));
        mockMvc.perform(post("/node" )
                .content(mapper.writeValueAsString(nodeInsertDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("nameSource")
    public void shouldNotCreateGivenWrongName(String name) throws Exception {

        NodeInsertDto nodeInsertDto = getNodeInsertDto(2, "Description"
                , "Name");
        nodeInsertDto.setName(name);
        NodeDto nodeDto = getNodeDto(1,2, "Description"
                , "Name", Collections.emptyList());

        when(nodeService.saveNode(nodeInsertDto))
                .thenReturn(nodeDto);
        mockMvc.perform(post("/node" )
                .content(mapper.writeValueAsString(nodeInsertDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    public static Stream<Arguments> nameSource()
    {
        return Stream.of(Arguments.of("", null, IntStream.range(0,51)
                .boxed()
                .map(s-> " ")
                .collect(Collectors.joining())));
    }

    @ParameterizedTest
    @MethodSource("descriptionSource")
    public void shouldNotCreateGivenWrongDescription(String descriptionSource) throws Exception {

        NodeInsertDto nodeInsertDto = getNodeInsertDto(2, "Description"
                , "Name");
        nodeInsertDto.setDescription(descriptionSource);
        NodeDto nodeDto = getNodeDto(1,2, "Description"
                , "Name", Collections.emptyList());

        when(nodeService.saveNode(nodeInsertDto))
                .thenReturn(nodeDto);
        mockMvc.perform(post("/node" )
                .content(mapper.writeValueAsString(nodeInsertDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    public static Stream<Arguments> descriptionSource()
    {
        return Stream.of(Arguments.of("", null, IntStream.range(0,251)
                .boxed()
                .map(s-> " ")
                .collect(Collectors.joining())));
    }

    @Test
    public void shouldUpdateNode() throws Exception {

        NodeUpdateDto nodeUpdateDto = getNodeUpdateDto(1
                ,2, "Description");
        NodeDto nodeDto = getNodeDto(1,2
                , "Description"
                , "Name", Collections.emptyList());

        when(nodeService.updateNode(any()))
                .thenReturn(nodeDto);
        mockMvc.perform(put("/node" )
                .content(mapper.writeValueAsString(nodeUpdateDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(nodeDto)));
    }

    @Test
    public void shouldNotUpdateNodeGivenAMissingNode() throws Exception {

        NodeUpdateDto nodeUpdateDto = getNodeUpdateDto(1
                ,2, "Description");

        when(nodeService.updateNode(nodeUpdateDto))
                .thenThrow(new NodeNotFoundException(2));
        mockMvc.perform(put("/node" )
                .content(mapper.writeValueAsString(nodeUpdateDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("descriptionUpdateSource")
    public void shouldNotUpdateNodeGivenAWrongDescription(String description) throws Exception {

        NodeUpdateDto nodeUpdateDto = getNodeUpdateDto(1
                ,2, "Description");
        nodeUpdateDto.setDescription(description);
        NodeDto nodeDto = getNodeDto(1,2
                , "Description"
                , "Name", Collections.emptyList());

        when(nodeService.updateNode(nodeUpdateDto))
                .thenReturn(nodeDto);
        mockMvc.perform(put("/node" )
                .content(mapper.writeValueAsString(nodeUpdateDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    public static Stream<Arguments> descriptionUpdateSource()
    {
        return Stream.of(Arguments.of( "", IntStream.range(0,251)
                .boxed()
                .map(s-> " ")
                .collect(Collectors.joining())));
    }

    @Test
    public void shouldNotUpdateNodeGivenAWrongId() throws Exception {

        NodeUpdateDto nodeUpdateDto = getNodeUpdateDto(1
                ,2, "Description");
        nodeUpdateDto.setId(null);
        NodeDto nodeDto = getNodeDto(1,2
                , "Description"
                , "Name", Collections.emptyList());

        when(nodeService.updateNode(nodeUpdateDto))
                .thenReturn(nodeDto);
        mockMvc.perform(put("/node" )
                .content(mapper.writeValueAsString(nodeUpdateDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findIdTree() throws Exception {

        NodeDto nodeDto = getNodeDto(1,2, "Description"
                , "Name", Collections.emptyList());
        when(nodeService.findTreeByNode(1))
                .thenReturn(nodeDto);
        mockMvc.perform(get("/node/tree/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(nodeDto)));
    }

    @Test
    public void shouldFailToFindIdTreeWhenIdNotFound() throws Exception {
        when(nodeService.findTreeByNode(1))
                .thenThrow(new NodeNotFoundException(1));

        mockMvc.perform(get("/node/tree/1"))
                .andExpect(status().isNotFound());
    }

}
