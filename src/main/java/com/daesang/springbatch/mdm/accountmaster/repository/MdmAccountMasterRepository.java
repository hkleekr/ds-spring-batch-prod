package com.daesang.springbatch.mdm.accountmaster.repository;

import com.daesang.springbatch.mdm.accountmaster.domain.MdmAccountMaster;
import com.daesang.springbatch.mdm.accountmaster.domain.MdmAccountMasterId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * fileName         : MdmAccountMaster
 * author           : inayoon
 * date             : 2022-10-19
 * description      : 고객정보 - Account 마스터 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-19       inayoon             최초생성
 * 2022-11-02       inayoon             Chunk 방식 변경
 * 2022-11-24       김수진과장            areaCode ID 추가
 */
public interface MdmAccountMasterRepository extends JpaRepository<MdmAccountMaster, MdmAccountMasterId> {
    List<MdmAccountMaster> findByLastUpdateDateBetween(String fromDate, String toDate);
}