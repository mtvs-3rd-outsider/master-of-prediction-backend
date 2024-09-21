package com.outsider.masterofpredictionbackend.betting.query.repository;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQueryRepository extends JpaRepository<User, Long>  {

}
