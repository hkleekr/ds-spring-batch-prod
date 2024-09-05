package com.daesang.springbatch.mdm.productSalesInfo.repository;

import com.daesang.springbatch.mdm.productSalesInfo.domain.MdmProductSalesInfo;
import com.daesang.springbatch.mdm.productSalesInfo.domain.MdmProductSalesInfoId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * fileName         : MdmProductSalesInfoRepository
 * author           : 권용성사원
 * date             : 2023-01-17
 * description      : 제품정보 - 제품 판매조직 기준 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-17       권용성사원             최초생성
 */
public interface MdmProductSalesInfoRepository extends JpaRepository<MdmProductSalesInfo, MdmProductSalesInfoId> {
}