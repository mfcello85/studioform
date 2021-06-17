package it.studioform.test.dto;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class NodeUpdateDto {

    private Integer id;

    private String description;

    private Integer parentId;

    @NotNull(groups = NodeDto.UpdateNodeValidationGroup.class)
    public Integer getId() {
        return id;
    }

    @NotNull(groups = {NodeDto.NewNodeValidationGroup.class, NodeDto.UpdateNodeValidationGroup.class})
    @Size(max=250, min= 1,groups = {NodeDto.NewNodeValidationGroup.class, NodeDto.UpdateNodeValidationGroup.class})
    public String getDescription() {
        return description;
    }

    public Integer getParentId() {
        return parentId;
    }
}
