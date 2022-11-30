import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './transformer.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransformerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const transformerEntity = useAppSelector(state => state.transformer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transformerDetailsHeading">Transformer</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{transformerEntity.id}</dd>
          <dt>
            <span id="fbus">Fbus</span>
          </dt>
          <dd>{transformerEntity.fbus}</dd>
          <dt>
            <span id="tbus">Tbus</span>
          </dt>
          <dd>{transformerEntity.tbus}</dd>
          <dt>
            <span id="min">Min</span>
          </dt>
          <dd>{transformerEntity.min}</dd>
          <dt>
            <span id="max">Max</span>
          </dt>
          <dd>{transformerEntity.max}</dd>
          <dt>
            <span id="totalTaps">Total Taps</span>
          </dt>
          <dd>{transformerEntity.totalTaps}</dd>
          <dt>
            <span id="tap">Tap</span>
          </dt>
          <dd>{transformerEntity.tap}</dd>
          <dt>
            <span id="manufactureYear">Manufacture Year</span>
          </dt>
          <dd>{transformerEntity.manufactureYear}</dd>
          <dt>
            <span id="commissioningYear">Commissioning Year</span>
          </dt>
          <dd>{transformerEntity.commissioningYear}</dd>
          <dt>Network</dt>
          <dd>{transformerEntity.network ? transformerEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/transformer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transformer/${transformerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransformerDetail;
