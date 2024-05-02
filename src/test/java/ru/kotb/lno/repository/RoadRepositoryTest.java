package ru.kotb.lno.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.kotb.lno.db.entity.MotorRoad;
import ru.kotb.lno.db.repository.RoadRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
@Transactional
class RoadRepositoryTest {

    private final RoadRepository roadRepository;

    @Autowired
    public RoadRepositoryTest(RoadRepository roadRepository) {
        this.roadRepository = roadRepository;
    }

    @Test
    public void repositoryNotNull() {
        Assertions.assertThat(roadRepository).isNotNull();
    }

    @Test
    public void findByIdReturnEntity() {
        MotorRoad entity = new MotorRoad();

        entity = roadRepository.save(entity);
        entity = roadRepository.findById(entity.getId()).get();

        Assertions.assertThat(entity).isNotNull();
    }

    @Test
    public void findByIdNonExistedEntity() {
        Optional<MotorRoad> entity = roadRepository.findById(0);

        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    public void findAllReturnMoreThenOneEntity() {
        MotorRoad entity = new MotorRoad();
        MotorRoad entity2 = new MotorRoad();

        roadRepository.save(entity);
        roadRepository.save(entity2);

        List<MotorRoad> entityList = roadRepository.findAll();

        Assertions.assertThat(entityList).isNotNull();
        Assertions.assertThat(entityList.size()).isEqualTo(2);
    }

    @Test
    public void saveSameEntityTwoTimesReturnsSameEntity() {
        MotorRoad entity = new MotorRoad();

        MotorRoad savedTestEntity1 = roadRepository.save(entity);
        MotorRoad savedTestEntity2 = roadRepository.save(entity);

        Assertions.assertThat(savedTestEntity1).isEqualTo(savedTestEntity2);
    }

    @Test
    public void findAllReturnEmptyList() {
        List<MotorRoad> entityList = roadRepository.findAll();

        Assertions.assertThat(entityList.size()).isEqualTo(0);
    }

    @Test
    public void saveReturnEntityWithID() {
        MotorRoad entity = new MotorRoad();

        MotorRoad savedEntity = roadRepository.save(entity);

        Assertions.assertThat(savedEntity.getId()).isNotNull();
    }

    @Test
    public void updateEntityReturnEntityNotNull() {
        MotorRoad entity = new MotorRoad();
        entity = roadRepository.save(entity);

        MotorRoad entitySave = roadRepository.findById(entity.getId()).get();
        entitySave.setName("New name");

        MotorRoad updatedTestEntity = roadRepository.save(entitySave);

        Assertions.assertThat(updatedTestEntity.getName()).isNotNull();
        Assertions.assertThat(updatedTestEntity.getId())
                .isEqualTo(entitySave.getId());
        Assertions.assertThat(updatedTestEntity.getName())
                .isEqualTo("New name");
    }

    @Test
    public void deleteReturnEntityIsNull() {
        MotorRoad entity = new MotorRoad();

        MotorRoad savedEntity = roadRepository.save(entity);

        assertAll(() -> roadRepository.deleteById(savedEntity.getId()));
    }
}