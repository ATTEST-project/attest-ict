import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './flex-profile.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FlexProfileDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const flexProfileEntity = useAppSelector(state => state.flexProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="flexProfileDetailsHeading">FlexProfile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{flexProfileEntity.id}</dd>
          <dt>
            <span id="season">Season</span>
          </dt>
          <dd>{flexProfileEntity.season}</dd>
          <dt>
            <span id="typicalDay">Typical Day</span>
          </dt>
          <dd>{flexProfileEntity.typicalDay}</dd>
          <dt>
            <span id="mode">Mode</span>
          </dt>
          <dd>{flexProfileEntity.mode}</dd>
          <dt>
            <span id="timeInterval">Time Interval</span>
          </dt>
          <dd>{flexProfileEntity.timeInterval}</dd>
          <dt>
            <span id="uploadDateTime">Upload Date Time</span>
          </dt>
          <dd>
            {flexProfileEntity.uploadDateTime ? (
              <TextFormat value={flexProfileEntity.uploadDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Input File</dt>
          <dd>{flexProfileEntity.inputFile ? flexProfileEntity.inputFile.fileName : ''}</dd>
          <dt>Network</dt>
          <dd>{flexProfileEntity.network ? flexProfileEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/flex-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/flex-profile/${flexProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FlexProfileDetail;
