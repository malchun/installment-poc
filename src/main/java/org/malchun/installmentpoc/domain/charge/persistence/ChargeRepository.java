package org.malchun.installmentpoc.domain.charge.persistence;

import org.malchun.installmentpoc.domain.charge.api.ChargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<ChargeEntity, String> {

    ChargeEntity getById(String id);

    List<ChargeEntity> findByParentWorkflowId(String workflowId);

    ChargeEntity save(ChargeEntity chargeEntity);
}
