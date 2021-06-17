package it.studioform.test.dto;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@NoArgsConstructor
public class NodeInsertDto extends NodeUpdateDto {

    private String name;

    @NotNull(groups = NodeDto.NewNodeValidationGroup.class)
    @Size(max=50, min= 1, groups = NodeDto.NewNodeValidationGroup.class)
    public String getName() {
        return name;
    }
}
