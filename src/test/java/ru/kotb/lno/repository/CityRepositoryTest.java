package ru.kotb.lno.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.kotb.lno.db.entity.City;
import ru.kotb.lno.db.repository.CityRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
@Transactional
class CityRepositoryTest {

    private final CityRepository cityRepository;

    @Autowired
    public CityRepositoryTest(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Test
    public void repositoryNotNull() {
        Assertions.assertThat(cityRepository).isNotNull();
    }

    @Test
    public void findByIdReturnEntity() {
        City entity = new City();

        entity = cityRepository.save(entity);
        entity = cityRepository.findById(entity.getId()).get();

        Assertions.assertThat(entity).isNotNull();
    }

    @Test
    public void findByIdNonExistedEntity() {
        Optional<City> entity = cityRepository.findById(0);

        Assertions.assertThat(entity).isEmpty();
    }

    @Test
    public void findAllReturnMoreThenOneEntity() {
        City entity = new City();
        City entity2 = new City();

        cityRepository.save(entity);
        cityRepository.save(entity2);

        List<City> entityList = cityRepository.findAll();

        Assertions.assertThat(entityList).isNotNull();
        Assertions.assertThat(entityList.size()).isEqualTo(2);
    }

    @Test
    public void saveSameEntityTwoTimesReturnsSameEntity() {
        City entity = new City();

        City savedTestEntity1 = cityRepository.save(entity);
        City savedTestEntity2 = cityRepository.save(entity);

        Assertions.assertThat(savedTestEntity1).isEqualTo(savedTestEntity2);
    }

    @Test
    public void findAllReturnEmptyList() {
        List<City> entityList = cityRepository.findAll();

        Assertions.assertThat(entityList.size()).isEqualTo(0);
    }

    @Test
    public void saveReturnEntityWithID() {
        City entity = new City();

        City savedEntity = cityRepository.save(entity);

        Assertions.assertThat(savedEntity.getId()).isNotNull();
    }

    @Test
    public void updateEntityReturnEntityNotNull() {
        City entity = new City();
        entity = cityRepository.save(entity);

        City entitySave = cityRepository.findById(entity.getId()).get();
        entitySave.setName("New name");

        City updatedTestEntity = cityRepository.save(entitySave);

        Assertions.assertThat(updatedTestEntity.getName()).isNotNull();
        Assertions.assertThat(updatedTestEntity.getId())
                .isEqualTo(entitySave.getId());
        Assertions.assertThat(updatedTestEntity.getName())
                .isEqualTo("New name");
    }

    @Test
    public void deleteReturnEntityIsNull() {
        City entity = new City();

        City savedEntity = cityRepository.save(entity);

        assertAll(() -> cityRepository.deleteById(savedEntity.getId()));
    }
}