import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './capacitor-bank-data.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CapacitorBankDataDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const capacitorBankDataEntity = useAppSelector(state => state.capacitorBankData.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="capacitorBankDataDetailsHeading">CapacitorBankData</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{capacitorBankDataEntity.id}</dd>
          <dt>
            <span id="busNum">Bus Num</span>
          </dt>
          <dd>{capacitorBankDataEntity.busNum}</dd>
          <dt>
            <span id="nodeId">Node Id</span>
          </dt>
          <dd>{capacitorBankDataEntity.nodeId}</dd>
          <dt>
            <span id="bankId">Bank Id</span>
          </dt>
          <dd>{capacitorBankDataEntity.bankId}</dd>
          <dt>
            <span id="qnom">Qnom</span>
          </dt>
          <dd>{capacitorBankDataEntity.qnom}</dd>
          <dt>Network</dt>
          <dd>{capacitorBankDataEntity.network ? capacitorBankDataEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/capacitor-bank-data" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/capacitor-bank-data/${capacitorBankDataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CapacitorBankDataDetail;
