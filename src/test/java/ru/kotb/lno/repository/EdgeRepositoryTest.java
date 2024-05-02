package ru.kotb.lno.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.kotb.lno.db.entity.Edge;
import ru.kotb.lno.db.entity.Node;
import ru.kotb.lno.db.repository.EdgeRepository;
import ru.kotb.lno.db.repository.NodeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
@Transactional
class EdgeRepositoryTest {

    private static final Node firstNode = new Node();

    private static final Node secondNode = new Node();

    private final EdgeRepository edgeRepository;

    private final NodeRepository nodeRepository;


    @Autowired
    public EdgeRepositoryTest(EdgeRepository edgeRepository, NodeRepository nodeRepository) {
        this.edgeRepository = edgeRepository;
        this.nodeRepository = nodeRepository;
    }

    @BeforeEach
    public void init() {
        nodeRepository.save(firstNode);
        nodeRepository.save(secondNode);
    }

    @Test
    public void repositoryNotNull() {
        Assertions.assertThat(edgeRepository).isNotNull();
    }

    @Test
    public void findByIdReturnEntity() {
        Edge entity = new Edge(firstNode, secondNode);

        entity = edgeRepository.save(entity);
        entity = edgeRepository.findById(entity.getEdgePK()).get();

        Assertions.assertThat(entity).isNotNull();
    }

    @Test
    public void findByIdNonExistedEntity() {
        Optional<Edge> entity = edgeRepository.findById(new Edge.EdgePK(firstNode, secondNode));

        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    public void saveSameEntityTwoTimesReturnsSameEntity() {
        Edge entity = new Edge(firstNode, secondNode);

        Edge savedTestEntity1 = edgeRepository.save(entity);
        Edge savedTestEntity2 = edgeRepository.save(entity);

        Assertions.assertThat(savedTestEntity1).isEqualTo(savedTestEntity2);
    }

    @Test
    public void findAllReturnEmptyList() {
        List<Edge> entityList = edgeRepository.findAll();

        Assertions.assertThat(entityList.size()).isEqualTo(0);
    }

    @Test
    public void saveReturnEntityWithID() {
        Edge entity = new Edge(firstNode, secondNode);

        Edge savedEntity = edgeRepository.save(entity);

        Assertions.assertThat(savedEntity.getEdgePK()).isNotNull();
    }

    @Test
    public void updateEntityReturnEntityNotNull() {
        Edge entity = new Edge(firstNode, secondNode);
        entity = edgeRepository.save(entity);

        Edge entitySave = edgeRepository.findById(entity.getEdgePK()).get();
        entitySave.setName("New name");

        Edge updatedTestEntity = edgeRepository.save(entitySave);

        Assertions.assertThat(updatedTestEntity.getName()).isNotNull();
        Assertions.assertThat(updatedTestEntity.getEdgePK())
                .isEqualTo(entitySave.getEdgePK());
        Assertions.assertThat(updatedTestEntity.getName())
                .isEqualTo("New name");
    }

    @Test
    public void deleteReturnEntityIsNull() {
        Edge entity = new Edge(firstNode, secondNode);

        Edge savedEntity = edgeRepository.save(entity);

        assertAll(() -> edgeRepository.deleteById(savedEntity.getEdgePK()));
    }
}