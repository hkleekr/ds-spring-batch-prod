package com.daesang.springbatch.mdm.productmaster.repository;

import com.daesang.springbatch.mdm.productmaster.domain.MdmProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * fileName         : MdmProductMasterRepository
 * author           : inayoon
 * date             : 2022-10-21
 * description      : 제품정보 - 제품 마스터 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-21       inayoon             최초생성
 * 2022-11-02       inayoon             Chunk 방식 변경
 */
public interface MdmProductMasterRepository extends JpaRepository<MdmProductMaster, String> {
    List<MdmProductMaster> findByLastUpdateDateBetween(String fromDate, String toDate);
}