package zatribune.spring.kitchenmaster.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import zatribune.spring.kitchenmaster.data.entities.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
class UserReactiveRepositoryIT {

    @Autowired
    UserReactiveRepository repository;
    String name ="user1@gmail.com";


    @BeforeEach
    void setUp() {
         repository.deleteAll().block();
    }

    @Test
    void save(){
        User user=new User();
        user.setUsername(name);
        User returnedUser=repository.save(user).block();
        Long count=repository.count().block();
        assertEquals(1L,count);
        assertNotNull(returnedUser);
        assertNotNull(returnedUser.getId());
    }
    @Test
    void saveAllAndFindAll(){
        User r1=new User();
        r1.setUsername("user1@gmail.com");
        User r2=new User();
        r2.setUsername("user2@gmail.com");
        User r3=new User();
        r3.setUsername("user3@gmail.com");
        //saveAll()
        repository.saveAll(List.of(r1,r2,r3)).subscribe();//ids are created
        Long count=repository.count().block();
        assertEquals(3L,count);
        //findAll()
        List<User>list=repository.findAll().collectList().block();
        assertNotNull(list);
        assertEquals(3,list.size());
    }

    @Test
    void findByUsername(){
        User user=new User();
        user.setUsername(name);
        repository.save(user).block();
        User returnedUser=repository.findDistinctByUsername(name).block();
        assertNotNull(returnedUser);
        assertEquals(name,returnedUser.getUsername());
    }

}