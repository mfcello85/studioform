package it.studioform.test.exceptions;

import lombok.Getter;

@Getter
public class NodeNotFoundException extends NotFoundException {
    public static final String MESSAGE = "Node not found for the id: %s";
    private final int id;

    public NodeNotFoundException(Integer id){
         super(String.format(MESSAGE,id));
         this.id = id;
    }
}
