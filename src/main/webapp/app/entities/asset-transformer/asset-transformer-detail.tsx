import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './asset-transformer.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AssetTransformerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const assetTransformerEntity = useAppSelector(state => state.assetTransformer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="assetTransformerDetailsHeading">AssetTransformer</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{assetTransformerEntity.id}</dd>
          <dt>
            <span id="busNum">Bus Num</span>
          </dt>
          <dd>{assetTransformerEntity.busNum}</dd>
          <dt>
            <span id="voltageRatio">Voltage Ratio</span>
          </dt>
          <dd>{assetTransformerEntity.voltageRatio}</dd>
          <dt>
            <span id="insulationMedium">Insulation Medium</span>
          </dt>
          <dd>{assetTransformerEntity.insulationMedium}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{assetTransformerEntity.type}</dd>
          <dt>
            <span id="indoorOutdoor">Indoor Outdoor</span>
          </dt>
          <dd>{assetTransformerEntity.indoorOutdoor}</dd>
          <dt>
            <span id="annualMaxLoadKva">Annual Max Load Kva</span>
          </dt>
          <dd>{assetTransformerEntity.annualMaxLoadKva}</dd>
          <dt>
            <span id="age">Age</span>
          </dt>
          <dd>{assetTransformerEntity.age}</dd>
          <dt>
            <span id="externalCondition">External Condition</span>
          </dt>
          <dd>{assetTransformerEntity.externalCondition}</dd>
          <dt>
            <span id="ratingKva">Rating Kva</span>
          </dt>
          <dd>{assetTransformerEntity.ratingKva}</dd>
          <dt>
            <span id="numConnectedCustomers">Num Connected Customers</span>
          </dt>
          <dd>{assetTransformerEntity.numConnectedCustomers}</dd>
          <dt>
            <span id="numSensitiveCustomers">Num Sensitive Customers</span>
          </dt>
          <dd>{assetTransformerEntity.numSensitiveCustomers}</dd>
          <dt>
            <span id="backupSupply">Backup Supply</span>
          </dt>
          <dd>{assetTransformerEntity.backupSupply}</dd>
          <dt>
            <span id="costOfFailureEuro">Cost Of Failure Euro</span>
          </dt>
          <dd>{assetTransformerEntity.costOfFailureEuro}</dd>
          <dt>Network</dt>
          <dd>{assetTransformerEntity.network ? assetTransformerEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/asset-transformer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/asset-transformer/${assetTransformerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssetTransformerDetail;
