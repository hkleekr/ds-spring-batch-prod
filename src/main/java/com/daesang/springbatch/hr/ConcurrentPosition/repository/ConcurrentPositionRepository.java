package com.daesang.springbatch.hr.ConcurrentPosition.repository;

import com.daesang.springbatch.hr.ConcurrentPosition.domain.ConcurrentPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * fileName         : OtherDepartmentRepository
 * author           : 권용성사원
 * date             : 2022-10-26
 * description      : 통합인사 겸직 배치 Repository
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 */
public interface ConcurrentPositionRepository extends JpaRepository<ConcurrentPosition, String> {
    List<ConcurrentPosition> findAll();
}
