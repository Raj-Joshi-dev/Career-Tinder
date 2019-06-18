package core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import core.entities.CTMatchingEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Career Tinder
 *
 */

@Repository
public interface MatchingRepository extends JpaRepository<CTMatchingEntity, Long>, CrudRepository<CTMatchingEntity, Long> {

    @Transactional
    @Query(value = "SELECT * FROM ctmatching WHERE company_id = ?1 AND applicant_id = ?2", nativeQuery = true)
    public CTMatchingEntity getMatchRow(Long companyId, Long applicantId);
    
    @Transactional
    @Query(value = "SELECT * from ctmatching where applicant_id = ?1", nativeQuery = true)
    public List<CTMatchingEntity> getRecordsForApplicant(Long applicantId);
    
    @Transactional
    @Query(value = "SELECT * from ctmatching where company_id = ?1", nativeQuery = true)
    public List<CTMatchingEntity> getRecordsForCompany(Long companyId);
    
   
    
    
}