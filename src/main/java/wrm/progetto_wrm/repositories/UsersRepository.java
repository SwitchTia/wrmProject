package wrm.progetto_wrm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wrm.progetto_wrm.entities.Users;


public interface UsersRepository extends JpaRepository <Users, Integer> {
    Users findByEmail (String email);
    boolean existsByEmail (String email);

}
