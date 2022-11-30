import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './storage.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const StorageDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const storageEntity = useAppSelector(state => state.storage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storageDetailsHeading">Storage</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{storageEntity.id}</dd>
          <dt>
            <span id="busNum">Bus Num</span>
          </dt>
          <dd>{storageEntity.busNum}</dd>
          <dt>
            <span id="ps">Ps</span>
          </dt>
          <dd>{storageEntity.ps}</dd>
          <dt>
            <span id="qs">Qs</span>
          </dt>
          <dd>{storageEntity.qs}</dd>
          <dt>
            <span id="energy">Energy</span>
          </dt>
          <dd>{storageEntity.energy}</dd>
          <dt>
            <span id="eRating">E Rating</span>
          </dt>
          <dd>{storageEntity.eRating}</dd>
          <dt>
            <span id="chargeRating">Charge Rating</span>
          </dt>
          <dd>{storageEntity.chargeRating}</dd>
          <dt>
            <span id="dischargeRating">Discharge Rating</span>
          </dt>
          <dd>{storageEntity.dischargeRating}</dd>
          <dt>
            <span id="chargeEfficiency">Charge Efficiency</span>
          </dt>
          <dd>{storageEntity.chargeEfficiency}</dd>
          <dt>
            <span id="thermalRating">Thermal Rating</span>
          </dt>
          <dd>{storageEntity.thermalRating}</dd>
          <dt>
            <span id="qmin">Qmin</span>
          </dt>
          <dd>{storageEntity.qmin}</dd>
          <dt>
            <span id="qmax">Qmax</span>
          </dt>
          <dd>{storageEntity.qmax}</dd>
          <dt>
            <span id="r">R</span>
          </dt>
          <dd>{storageEntity.r}</dd>
          <dt>
            <span id="x">X</span>
          </dt>
          <dd>{storageEntity.x}</dd>
          <dt>
            <span id="pLoss">P Loss</span>
          </dt>
          <dd>{storageEntity.pLoss}</dd>
          <dt>
            <span id="qLoss">Q Loss</span>
          </dt>
          <dd>{storageEntity.qLoss}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{storageEntity.status}</dd>
          <dt>
            <span id="socInitial">Soc Initial</span>
          </dt>
          <dd>{storageEntity.socInitial}</dd>
          <dt>
            <span id="socMin">Soc Min</span>
          </dt>
          <dd>{storageEntity.socMin}</dd>
          <dt>
            <span id="socMax">Soc Max</span>
          </dt>
          <dd>{storageEntity.socMax}</dd>
          <dt>Network</dt>
          <dd>{storageEntity.network ? storageEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/storage" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/storage/${storageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StorageDetail;
