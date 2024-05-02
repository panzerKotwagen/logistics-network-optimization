package ru.kotb.lno.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kotb.lno.db.entity.Node;


@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {

}
