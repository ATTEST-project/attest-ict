import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './flex-cost.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FlexCostDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const flexCostEntity = useAppSelector(state => state.flexCost.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="flexCostDetailsHeading">FlexCost</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{flexCostEntity.id}</dd>
          <dt>
            <span id="busNum">Bus Num</span>
          </dt>
          <dd>{flexCostEntity.busNum}</dd>
          <dt>
            <span id="model">Model</span>
          </dt>
          <dd>{flexCostEntity.model}</dd>
          <dt>
            <span id="nCost">N Cost</span>
          </dt>
          <dd>{flexCostEntity.nCost}</dd>
          <dt>
            <span id="costPr">Cost Pr</span>
          </dt>
          <dd>{flexCostEntity.costPr}</dd>
          <dt>
            <span id="costQr">Cost Qr</span>
          </dt>
          <dd>{flexCostEntity.costQr}</dd>
          <dt>
            <span id="costPf">Cost Pf</span>
          </dt>
          <dd>{flexCostEntity.costPf}</dd>
          <dt>
            <span id="costQf">Cost Qf</span>
          </dt>
          <dd>{flexCostEntity.costQf}</dd>
          <dt>Flex Profile</dt>
          <dd>{flexCostEntity.flexProfile ? flexCostEntity.flexProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/flex-cost" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/flex-cost/${flexCostEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FlexCostDetail;
