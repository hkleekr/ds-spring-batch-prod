package com.daesang.springbatch.hr.ConcurrentPosition.repository;

import com.daesang.springbatch.hr.ConcurrentPosition.domain.ConcurrentPosition;
import com.daesang.springbatch.hr.ConcurrentPosition.domain.QConcurrentPosition;
import com.daesang.springbatch.hr.employee.domain.QEmployee;
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
 * fileName         : ConcurrentPositionRepositorySupport
 * author           : 김수진과장
 * date             : 2023-02-16
 * description      : 통합인사 임직원 겸직 배치 Join
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-02-16       권용성사원             최초생성
 */
@Repository
public class ConcurrentPositionRepositorySupport extends QuerydslRepositorySupport {

    private EntityManager em;

    public ConcurrentPositionRepositorySupport() {
        super(ConcurrentPosition.class);
    }
    private JPAQueryFactory queryFactory;

    @PersistenceContext(unitName = "hrEntityManager")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
        em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    QEmployee employee = QEmployee.employee;
    QConcurrentPosition concurrentPosition = QConcurrentPosition.concurrentPosition;

    public List<ConcurrentPosition> findAllOnOrgNumber() {

        return from(concurrentPosition)
              .join(employee)
              .on(concurrentPosition.companyCode.eq(employee.companyCode), concurrentPosition.orgNumber.eq(employee.orgNumber))
              .fetch();
    }

}
