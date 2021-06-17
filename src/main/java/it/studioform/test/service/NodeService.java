package it.studioform.test.service;

import it.studioform.test.dto.NodeDto;
import it.studioform.test.dto.NodeInsertDto;
import it.studioform.test.dto.NodeUpdateDto;
import it.studioform.test.entity.Node;
import it.studioform.test.exceptions.NodeNotFoundException;
import it.studioform.test.exceptions.NotFoundException;
import it.studioform.test.mapper.NodeMapper;
import it.studioform.test.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final NodeRepository nodeRepository;
    private final NodeMapper nodeMapper;

    @Transactional(readOnly = true)
    public NodeDto findById(Integer id) {
        Node node = getNode(id);
        return nodeMapper.mapToNode(node);
    }

    private Node getNode(Integer id) {
        return nodeRepository.findById(id)
                    .orElseThrow(() -> new NodeNotFoundException(id));
    }

    @Transactional
    public NodeDto saveNode(NodeInsertDto nodeDto) {
        failIfParentNotExist(nodeDto);

        return nodeMapper.mapToNode(nodeRepository.save(nodeMapper.mapToNewNode(nodeDto)));
    }

    @Transactional(readOnly = true)
    public NodeDto findTreeByNode(Integer id) {
        Node node = nodeRepository.findByIdRecursive(id)
                .orElseThrow(()->new NodeNotFoundException(id));
        return nodeMapper.mapToNodeTree(node);
    }

    @Transactional
    public NodeDto updateNode(NodeUpdateDto nodeDto) {
        failIfParentNotExist(nodeDto);
        Node node = getNode(nodeDto.getId());
        node.setDescription(nodeDto.getDescription());

        if(node.getParentId()!=null){
            node.setParentId(node.getParentId());
        }

        return nodeMapper.mapToNode(nodeRepository.save(node));
     }

    private void failIfParentNotExist(NodeUpdateDto nodeDto) {
        if (nodeDto.getParentId() != null) {
            getNode(nodeDto.getParentId());
        }
    }
}
