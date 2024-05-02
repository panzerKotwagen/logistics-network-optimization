package ru.kotb.lno.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.kotb.lno.db.entity.Node;
import ru.kotb.lno.db.repository.NodeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
@Transactional
class NodeRepositoryTest {

    private final NodeRepository nodeRepository;

    @Autowired
    public NodeRepositoryTest(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }


    @Test
    public void repositoryNotNull() {
        Assertions.assertThat(nodeRepository).isNotNull();
    }

    @Test
    public void findByIdReturnEntity() {
        Node entity = new Node();

        entity = nodeRepository.save(entity);
        entity = nodeRepository.findById(entity.getId()).get();
        Assertions.assertThat(entity).isNotNull();
    }

    @Test
    public void findByIdNonExistedEntity() {
        Optional<Node> entity = nodeRepository.findById(0);

        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    public void findAllReturnMoreThenOneEntity() {
        Node entity = new Node();
        Node entity2 = new Node();

        nodeRepository.save(entity);
        nodeRepository.save(entity2);

        List<Node> entityList = nodeRepository.findAll();

        Assertions.assertThat(entityList).isNotNull();
        Assertions.assertThat(entityList.size()).isEqualTo(2);
    }

    @Test
    public void saveSameEntityTwoTimesReturnsSameEntity() {
        Node entity = new Node();

        Node savedTestEntity1 = nodeRepository.save(entity);
        Node savedTestEntity2 = nodeRepository.save(entity);

        Assertions.assertThat(savedTestEntity1).isEqualTo(savedTestEntity2);
    }

    @Test
    public void findAllReturnEmptyList() {
        List<Node> entityList = nodeRepository.findAll();

        Assertions.assertThat(entityList.size()).isEqualTo(0);
    }

    @Test
    public void saveReturnEntityWithID() {
        Node entity = new Node();

        Node savedEntity = nodeRepository.save(entity);

        Assertions.assertThat(savedEntity.getId()).isNotNull();
    }

    @Test
    public void updateEntityReturnEntityNotNull() {
        Node entity = new Node();
        entity = nodeRepository.save(entity);

        Node entitySave = nodeRepository.findById(entity.getId()).get();
        entitySave.setName("New name");

        Node updatedTestEntity = nodeRepository.save(entitySave);

        Assertions.assertThat(updatedTestEntity.getName()).isNotNull();
        Assertions.assertThat(updatedTestEntity.getId())
                .isEqualTo(entitySave.getId());
        Assertions.assertThat(updatedTestEntity.getName())
                .isEqualTo("New name");
    }

    @Test
    public void deleteReturnEntityIsNull() {
        Node entity = new Node();

        Node savedEntity = nodeRepository.save(entity);

        assertAll(() -> nodeRepository.deleteById(savedEntity.getId()));
    }
}