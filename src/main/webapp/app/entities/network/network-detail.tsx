import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './network.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const NetworkDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const networkEntity = useAppSelector(state => state.network.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="networkDetailsHeading">Network</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{networkEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{networkEntity.name}</dd>
          <dt>
            <span id="mpcName">Mpc Name</span>
          </dt>
          <dd>{networkEntity.mpcName}</dd>
          <dt>
            <span id="country">Country</span>
          </dt>
          <dd>{networkEntity.country}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{networkEntity.type}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{networkEntity.description}</dd>

          {/* Comment 2023/10/12
              The functionality for logical deletion of the network has not been implemented yet
              <dt>
                <span id="isDeleted">Is Deleted</span>
              </dt>
              <dd>{networkEntity.isDeleted ? 'true' : 'false'}</dd>
          */}
          <dt>
            <span id="networkDate">Network Date</span>
          </dt>
          <dd>
            {networkEntity.networkDate ? <TextFormat value={networkEntity.networkDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="version">Version</span>
          </dt>
          <dd>{networkEntity.version}</dd>
          <dt>
            <span id="creationDateTime">Creation Date Time</span>
          </dt>
          <dd>
            {networkEntity.creationDateTime ? (
              <TextFormat value={networkEntity.creationDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updateDateTime">Update Date Time</span>
          </dt>
          <dd>
            {networkEntity.updateDateTime ? <TextFormat value={networkEntity.updateDateTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/network" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/network/${networkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default NetworkDetail;
