package com.keystone.repository;

import com.keystone.entity.Priority;
import com.keystone.entity.SLA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SLARepository extends JpaRepository<SLA, Long> {

    Optional<SLA> findByPriorityAndActiveTrue(Priority priority);

    List<SLA> findByActiveTrue();

}