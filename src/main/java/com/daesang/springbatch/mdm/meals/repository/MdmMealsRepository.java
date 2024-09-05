package com.daesang.springbatch.mdm.meals.repository;

import com.daesang.springbatch.mdm.meals.domain.MdmMeals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * fileName         : MealsRepository
 * author           : 김수진과장
 * date             : 2022-11-02
 * descrition       : 고객정보 - 급식거래처 조회
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-02       김수진과장             최초생성
 * 2022-11-??       김수진과장            생성일자, 최종수정일자로 조회
 * 2022-11-14       김수진과장             마지막 수정일을 기준으로 조회 기능 추가
 */
public interface MdmMealsRepository extends JpaRepository<MdmMeals,String> {
    List<MdmMeals> findByLastmodificationdtimeBetween(String fromDate,String toDate);
}
