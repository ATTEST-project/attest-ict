import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './load-profile.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LoadProfileDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const loadProfileEntity = useAppSelector(state => state.loadProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="loadProfileDetailsHeading">LoadProfile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{loadProfileEntity.id}</dd>
          <dt>
            <span id="season">Season</span>
          </dt>
          <dd>{loadProfileEntity.season}</dd>
          <dt>
            <span id="typicalDay">Typical Day</span>
          </dt>
          <dd>{loadProfileEntity.typicalDay}</dd>
          <dt>
            <span id="mode">Mode</span>
          </dt>
          <dd>{loadProfileEntity.mode}</dd>
          <dt>
            <span id="timeInterval">Time Interval</span>
          </dt>
          <dd>{loadProfileEntity.timeInterval}</dd>
          <dt>
            <span id="uploadDateTime">Upload Date Time</span>
          </dt>
          <dd>
            {loadProfileEntity.uploadDateTime ? (
              <TextFormat value={loadProfileEntity.uploadDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Input File</dt>
          <dd>{loadProfileEntity.inputFile ? loadProfileEntity.inputFile.fileName : ''}</dd>
          <dt>Network</dt>
          <dd>{loadProfileEntity.network ? loadProfileEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/load-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/load-profile/${loadProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default LoadProfileDetail;
