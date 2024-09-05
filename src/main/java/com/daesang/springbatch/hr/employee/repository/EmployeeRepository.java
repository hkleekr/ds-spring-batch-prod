package com.daesang.springbatch.hr.employee.repository;

import com.daesang.springbatch.hr.employee.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * fileName         : EmployeeRepository
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 임직원 배치 Repository
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findAllByCompanyCodeAndUpdateDateAfter(String companyCode, LocalDate updateDate);
    List<Employee> findAllByUpdateDateAfter(LocalDate updateDate);
}
