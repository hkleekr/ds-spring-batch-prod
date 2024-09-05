package com.daesang.springbatch.hr.department.repository;

import com.daesang.springbatch.hr.department.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * fileName         : DepartmentRepository
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 임직원 배치 Repository
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 */
public interface DepartmentRepository extends JpaRepository<Department, String> {
    List<Department> findAll();
}
