package it.studioform.test.repository;

import it.studioform.test.entity.Node;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NodeRepository extends CrudRepository<Node, Integer> {

    Optional<Node> findById(Integer id);

    @EntityGraph(attributePaths = "list")
    @Query("select n from Node n where n.id=:id")
    Optional<Node> findByIdRecursive(@Param(value = "id") Integer id);
}
