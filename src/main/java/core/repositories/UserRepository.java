package core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import core.entities.CTUserEntity;

/*
 * UserRepository implementation for JPA transaction in User Entity
 * @author: Pravin Garad
 */

@Repository
public interface UserRepository extends JpaRepository<CTUserEntity, Long>, CrudRepository<CTUserEntity, Long> {
	
    @Transactional
    @Modifying
    @Query(value = "UPDATE ctuser ue SET ue.authtoken = ?1 WHERE ue.emailid = ?2", nativeQuery = true)  
    void updateUser(String authtoken, String email);
    
    @Transactional
    @Query(value = "SELECT * from ctuser where emailid = ?1 and password =?2", nativeQuery = true)  
    public CTUserEntity checkPassword(String email, String password);
}