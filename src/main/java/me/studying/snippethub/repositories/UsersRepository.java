package me.studying.snippethub.repositories;

import me.studying.snippethub.entity.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Long> {
}
