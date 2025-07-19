package com.example.MLProject.Repository;

import com.example.MLProject.Model.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserTable, Integer> {

}
