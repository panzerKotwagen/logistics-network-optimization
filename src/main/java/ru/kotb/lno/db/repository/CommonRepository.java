package ru.kotb.lno.db.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.kotb.lno.db.entity.AbstractEntity;


@NoRepositoryBean
public interface CommonRepository<E extends AbstractEntity> extends JpaRepository<E, Integer> {
}
