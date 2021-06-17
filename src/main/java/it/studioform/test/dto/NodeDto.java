package it.studioform.test.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NodeDto extends NodeInsertDto {

    private List<NodeDto> list;

    public interface NewNodeValidationGroup{

    }

    public interface UpdateNodeValidationGroup{

    }

}
