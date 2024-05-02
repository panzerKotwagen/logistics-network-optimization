package ru.kotb.lno.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kotb.lno.db.entity.Edge;


@Repository
public interface EdgeRepository extends JpaRepository<Edge, Edge.EdgePK> {

}
