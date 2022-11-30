import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './billing-consumption.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BillingConsumptionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const billingConsumptionEntity = useAppSelector(state => state.billingConsumption.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="billingConsumptionDetailsHeading">BillingConsumption</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{billingConsumptionEntity.id}</dd>
          <dt>
            <span id="busNum">Bus Num</span>
          </dt>
          <dd>{billingConsumptionEntity.busNum}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{billingConsumptionEntity.type}</dd>
          <dt>
            <span id="totalEnergyConsumption">Total Energy Consumption</span>
          </dt>
          <dd>{billingConsumptionEntity.totalEnergyConsumption}</dd>
          <dt>
            <span id="unitOfMeasure">Unit Of Measure</span>
          </dt>
          <dd>{billingConsumptionEntity.unitOfMeasure}</dd>
          <dt>Network</dt>
          <dd>{billingConsumptionEntity.network ? billingConsumptionEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/billing-consumption" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/billing-consumption/${billingConsumptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BillingConsumptionDetail;
