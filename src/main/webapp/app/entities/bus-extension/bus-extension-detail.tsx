import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './bus-extension.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BusExtensionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const busExtensionEntity = useAppSelector(state => state.busExtension.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="busExtensionDetailsHeading">BusExtension</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{busExtensionEntity.id}</dd>
          <dt>
            <span id="hasGen">Has Gen</span>
          </dt>
          <dd>{busExtensionEntity.hasGen}</dd>
          <dt>
            <span id="isLoad">Is Load</span>
          </dt>
          <dd>{busExtensionEntity.isLoad}</dd>
          <dt>
            <span id="snomMva">Snom Mva</span>
          </dt>
          <dd>{busExtensionEntity.snomMva}</dd>
          <dt>
            <span id="sx">Sx</span>
          </dt>
          <dd>{busExtensionEntity.sx}</dd>
          <dt>
            <span id="sy">Sy</span>
          </dt>
          <dd>{busExtensionEntity.sy}</dd>
          <dt>
            <span id="gx">Gx</span>
          </dt>
          <dd>{busExtensionEntity.gx}</dd>
          <dt>
            <span id="gy">Gy</span>
          </dt>
          <dd>{busExtensionEntity.gy}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{busExtensionEntity.status}</dd>
          <dt>
            <span id="incrementCost">Increment Cost</span>
          </dt>
          <dd>{busExtensionEntity.incrementCost}</dd>
          <dt>
            <span id="decrementCost">Decrement Cost</span>
          </dt>
          <dd>{busExtensionEntity.decrementCost}</dd>
          <dt>
            <span id="mRid">M Rid</span>
          </dt>
          <dd>{busExtensionEntity.mRid}</dd>
          <dt>Bus</dt>
          <dd>{busExtensionEntity.bus ? busExtensionEntity.bus.busNum : ''}</dd>
        </dl>
        <Button tag={Link} to="/bus-extension" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bus-extension/${busExtensionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BusExtensionDetail;
