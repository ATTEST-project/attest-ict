import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './asset-ug-cable.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AssetUGCableDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const assetUGCableEntity = useAppSelector(state => state.assetUGCable.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="assetUGCableDetailsHeading">AssetUGCable</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{assetUGCableEntity.id}</dd>
          <dt>
            <span id="sectionLabel">Section Label</span>
          </dt>
          <dd>{assetUGCableEntity.sectionLabel}</dd>
          <dt>
            <span id="circuitId">Circuit Id</span>
          </dt>
          <dd>{assetUGCableEntity.circuitId}</dd>
          <dt>
            <span id="conductorCrossSectionalArea">Conductor Cross Sectional Area</span>
          </dt>
          <dd>{assetUGCableEntity.conductorCrossSectionalArea}</dd>
          <dt>
            <span id="sheathMaterial">Sheath Material</span>
          </dt>
          <dd>{assetUGCableEntity.sheathMaterial}</dd>
          <dt>
            <span id="designVoltage">Design Voltage</span>
          </dt>
          <dd>{assetUGCableEntity.designVoltage}</dd>
          <dt>
            <span id="operatingVoltage">Operating Voltage</span>
          </dt>
          <dd>{assetUGCableEntity.operatingVoltage}</dd>
          <dt>
            <span id="insulationTypeSheath">Insulation Type Sheath</span>
          </dt>
          <dd>{assetUGCableEntity.insulationTypeSheath}</dd>
          <dt>
            <span id="conductorMaterial">Conductor Material</span>
          </dt>
          <dd>{assetUGCableEntity.conductorMaterial}</dd>
          <dt>
            <span id="age">Age</span>
          </dt>
          <dd>{assetUGCableEntity.age}</dd>
          <dt>
            <span id="faultHistory">Fault History</span>
          </dt>
          <dd>{assetUGCableEntity.faultHistory}</dd>
          <dt>
            <span id="lengthOfCableSectionMeters">Length Of Cable Section Meters</span>
          </dt>
          <dd>{assetUGCableEntity.lengthOfCableSectionMeters}</dd>
          <dt>
            <span id="sectionRating">Section Rating</span>
          </dt>
          <dd>{assetUGCableEntity.sectionRating}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{assetUGCableEntity.type}</dd>
          <dt>
            <span id="numberOfCores">Number Of Cores</span>
          </dt>
          <dd>{assetUGCableEntity.numberOfCores}</dd>
          <dt>
            <span id="netPerformanceCostOfFailureEuro">Net Performance Cost Of Failure Euro</span>
          </dt>
          <dd>{assetUGCableEntity.netPerformanceCostOfFailureEuro}</dd>
          <dt>
            <span id="repairTimeHour">Repair Time Hour</span>
          </dt>
          <dd>{assetUGCableEntity.repairTimeHour}</dd>
          <dt>Network</dt>
          <dd>{assetUGCableEntity.network ? assetUGCableEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/asset-ug-cable" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/asset-ug-cable/${assetUGCableEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssetUGCableDetail;
