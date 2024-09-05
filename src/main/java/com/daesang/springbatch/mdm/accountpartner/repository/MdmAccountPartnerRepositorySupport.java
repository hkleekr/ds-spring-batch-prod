package com.daesang.springbatch.mdm.accountpartner.repository;

import com.daesang.springbatch.mdm.accountmaster.domain.QMdmAccountMaster;
import com.daesang.springbatch.mdm.accountpartner.domain.MdmAccountPartner;
import com.daesang.springbatch.mdm.accountpartner.domain.QMdmAccountPartner;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * fileName         : MdmAccountPartnerRepositorySupport
 * author           : 김수진과장
 * date             : 2022-11-18
 * descrition       :고객정보 - Account 파트너 기능 배치
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-18       김수진과장             최초생성
 */
@Repository
public class MdmAccountPartnerRepositorySupport extends QuerydslRepositorySupport {

    private EntityManager em;

    public MdmAccountPartnerRepositorySupport() {
        super(MdmAccountPartner.class);
    }
    private JPAQueryFactory queryFactory;

    @PersistenceContext(unitName = "mdmEntityManager")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
        em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    QMdmAccountPartner mdmAccountPartner = QMdmAccountPartner.mdmAccountPartner;
    QMdmAccountMaster mdmAccountMaster = QMdmAccountMaster.mdmAccountMaster;

    public List<MdmAccountPartner> findByPartnerList(String fromDate, String toDate) {

        return from(mdmAccountPartner)
              .leftJoin(mdmAccountMaster)
              .on(mdmAccountPartner.mastid.eq(mdmAccountMaster.mdmCode), mdmAccountPartner.areaid.eq(mdmAccountMaster.areaCode))
              .where(mdmAccountPartner.parvw.eq("WE"), mdmAccountMaster.lastUpdateDate.between(fromDate,toDate))
              .fetch();
    }

}
