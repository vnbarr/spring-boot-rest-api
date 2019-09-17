package com.rest.api.example.domain.repository;

import com.rest.api.example.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by vbarros on 16/09/2019 .
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByOrderByNameAsc();

    @Modifying
    @Query("update com.rest.api.example.domain.model.Company c set c.deleted = true where c.id = :id and c.deleted is false")
    int softDeleteById(@Param("id") long id);

    @Modifying
    @Query("update com.rest.api.example.domain.model.Company c set c.deleted = true where c.id in :ids and c.deleted is false")
    int softDeleteByIds(@Param("ids") List<Long> ids);

    Company findByNameAndDeleted(String name, boolean deleted);

    List<Company> findAllByDeletedOrderByNameAsc(Boolean deleted);
}
