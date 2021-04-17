package zatribune.spring.kitchenmaster.data.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import zatribune.spring.kitchenmaster.data.entities.Role;


public interface RoleReactiveRepository extends ReactiveMongoRepository<Role,String> {
}
