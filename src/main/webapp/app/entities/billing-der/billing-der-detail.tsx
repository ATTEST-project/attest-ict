import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './billing-der.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BillingDerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const billingDerEntity = useAppSelector(state => state.billingDer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="billingDerDetailsHeading">BillingDer</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{billingDerEntity.id}</dd>
          <dt>
            <span id="busNum">Bus Num</span>
          </dt>
          <dd>{billingDerEntity.busNum}</dd>
          <dt>
            <span id="maxPowerKw">Max Power Kw</span>
          </dt>
          <dd>{billingDerEntity.maxPowerKw}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{billingDerEntity.type}</dd>
          <dt>Network</dt>
          <dd>{billingDerEntity.network ? billingDerEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/billing-der" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/billing-der/${billingDerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BillingDerDetail;
