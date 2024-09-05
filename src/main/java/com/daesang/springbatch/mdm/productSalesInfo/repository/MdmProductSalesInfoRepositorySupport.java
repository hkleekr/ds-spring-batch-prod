package com.daesang.springbatch.mdm.productSalesInfo.repository;

import com.daesang.springbatch.mdm.productSalesInfo.domain.MdmProductSalesInfo;
import com.daesang.springbatch.mdm.productSalesInfo.domain.QMdmProductSalesInfo;
import com.daesang.springbatch.mdm.productmaster.domain.QMdmProductMaster;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * fileName         : MdmProductMasterRepositorySupport
 * author           : 권용성사원
 * date             : 2023-01-17
 * description      : 제품정보 - 제품 판매조직 기준 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-17       권용성사원             최초생성
 */
@Repository
public class MdmProductSalesInfoRepositorySupport extends QuerydslRepositorySupport {
    private EntityManager em;

    public MdmProductSalesInfoRepositorySupport() {
        super(MdmProductSalesInfo.class);
    }

    private JPAQueryFactory queryFactory;

    @PersistenceContext(unitName = "mdmEntityManager")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
        em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    QMdmProductMaster mdmProductMaster = QMdmProductMaster.mdmProductMaster;

    QMdmProductSalesInfo mdmProductSalesInfo = QMdmProductSalesInfo.mdmProductSalesInfo;

    public List<MdmProductSalesInfo> findByLastUpdateDateBetween(String fromDate, String toDate) {

        return from(mdmProductSalesInfo)
                .leftJoin(mdmProductMaster)
                .on(mdmProductSalesInfo.mdmCode.eq(mdmProductMaster.mdmCode))
                .where(mdmProductSalesInfo.lastUpdateDate.between(fromDate,toDate), mdmProductMaster.mdmCode.isNull())
                .fetch();
    }
}