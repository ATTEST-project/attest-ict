import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './gen-cost.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenCostDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const genCostEntity = useAppSelector(state => state.genCost.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="genCostDetailsHeading">GenCost</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{genCostEntity.id}</dd>
          <dt>
            <span id="model">Model</span>
          </dt>
          <dd>{genCostEntity.model}</dd>
          <dt>
            <span id="startup">Startup</span>
          </dt>
          <dd>{genCostEntity.startup}</dd>
          <dt>
            <span id="shutdown">Shutdown</span>
          </dt>
          <dd>{genCostEntity.shutdown}</dd>
          <dt>
            <span id="nCost">N Cost</span>
          </dt>
          <dd>{genCostEntity.nCost}</dd>
          <dt>
            <span id="costPF">Cost PF</span>
          </dt>
          <dd>{genCostEntity.costPF}</dd>
          <dt>
            <span id="costQF">Cost QF</span>
          </dt>
          <dd>{genCostEntity.costQF}</dd>
          <dt>Generator</dt>
          <dd>{genCostEntity.generator ? genCostEntity.generator.busNum : ''}</dd>
        </dl>
        <Button tag={Link} to="/gen-cost" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/gen-cost/${genCostEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GenCostDetail;
