import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './bus.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { displayButton } from 'app/shared/reducers/back-button-display';

export const BusDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const busUrl = props.match.url.replace(/bus(\/.+)$/, 'bus');

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    dispatch(displayButton(false));
    return () => {
      dispatch(displayButton(true));
    };
  }, []);

  const busEntity = useAppSelector(state => state.bus.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="busDetailsHeading">Bus</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{busEntity.id}</dd>
          <dt>
            <span id="busNum">Bus Num</span>
          </dt>
          <dd>{busEntity.busNum}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{busEntity.type}</dd>
          <dt>
            <span id="activePower">Active Power</span>
          </dt>
          <dd>{busEntity.activePower}</dd>
          <dt>
            <span id="reactivePower">Reactive Power</span>
          </dt>
          <dd>{busEntity.reactivePower}</dd>
          <dt>
            <span id="conductance">Conductance</span>
          </dt>
          <dd>{busEntity.conductance}</dd>
          <dt>
            <span id="susceptance">Susceptance</span>
          </dt>
          <dd>{busEntity.susceptance}</dd>
          <dt>
            <span id="area">Area</span>
          </dt>
          <dd>{busEntity.area}</dd>
          <dt>
            <span id="vm">Vm</span>
          </dt>
          <dd>{busEntity.vm}</dd>
          <dt>
            <span id="va">Va</span>
          </dt>
          <dd>{busEntity.va}</dd>
          <dt>
            <span id="baseKv">Base Kv</span>
          </dt>
          <dd>{busEntity.baseKv}</dd>
          <dt>
            <span id="zone">Zone</span>
          </dt>
          <dd>{busEntity.zone}</dd>
          <dt>
            <span id="vmax">Vmax</span>
          </dt>
          <dd>{busEntity.vmax}</dd>
          <dt>
            <span id="vmin">Vmin</span>
          </dt>
          <dd>{busEntity.vmin}</dd>
          <dt>Network</dt>
          <dd>{busEntity.network ? busEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to={busUrl} replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`${busUrl}/${busEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BusDetail;
