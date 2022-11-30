import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './gen-profile.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenProfileDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const genProfileEntity = useAppSelector(state => state.genProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="genProfileDetailsHeading">GenProfile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{genProfileEntity.id}</dd>
          <dt>
            <span id="season">Season</span>
          </dt>
          <dd>{genProfileEntity.season}</dd>
          <dt>
            <span id="typicalDay">Typical Day</span>
          </dt>
          <dd>{genProfileEntity.typicalDay}</dd>
          <dt>
            <span id="mode">Mode</span>
          </dt>
          <dd>{genProfileEntity.mode}</dd>
          <dt>
            <span id="timeInterval">Time Interval</span>
          </dt>
          <dd>{genProfileEntity.timeInterval}</dd>
          <dt>
            <span id="uploadDateTime">Upload Date Time</span>
          </dt>
          <dd>
            {genProfileEntity.uploadDateTime ? (
              <TextFormat value={genProfileEntity.uploadDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Input File</dt>
          <dd>{genProfileEntity.inputFile ? genProfileEntity.inputFile.fileName : ''}</dd>
          <dt>Network</dt>
          <dd>{genProfileEntity.network ? genProfileEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/gen-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/gen-profile/${genProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GenProfileDetail;
