package sample.data.redis;

import org.springframework.data.repository.CrudRepository;

public interface SampleBeanRepository extends CrudRepository<SampleBean, String> {
}
