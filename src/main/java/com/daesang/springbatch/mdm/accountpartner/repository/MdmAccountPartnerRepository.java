package com.daesang.springbatch.mdm.accountpartner.repository;

import com.daesang.springbatch.mdm.accountpartner.domain.MdmAccountPartner;

import com.daesang.springbatch.mdm.accountpartner.domain.MdmAccountPartnerId;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * fileName         : MdmAccountPartnerRepository
 * author           : 김수진과장
 * date             : 2022-11-10
 * descrition       : 고객정보 - Account 파트너 기능 배치
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-10       김수진과장             최초생성
 */

public interface MdmAccountPartnerRepository extends JpaRepository<MdmAccountPartner,MdmAccountPartnerId> {

}
