import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './branch-profile.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BranchProfileDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const branchProfileEntity = useAppSelector(state => state.branchProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="branchProfileDetailsHeading">BranchProfile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{branchProfileEntity.id}</dd>
          <dt>
            <span id="season">Season</span>
          </dt>
          <dd>{branchProfileEntity.season}</dd>
          <dt>
            <span id="typicalDay">Typical Day</span>
          </dt>
          <dd>{branchProfileEntity.typicalDay}</dd>
          <dt>
            <span id="mode">Mode</span>
          </dt>
          <dd>{branchProfileEntity.mode}</dd>
          <dt>
            <span id="timeInterval">Time Interval</span>
          </dt>
          <dd>{branchProfileEntity.timeInterval}</dd>
          <dt>
            <span id="uploadDateTime">Upload Date Time</span>
          </dt>
          <dd>
            {branchProfileEntity.uploadDateTime ? (
              <TextFormat value={branchProfileEntity.uploadDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Input File</dt>
          <dd>{branchProfileEntity.inputFile ? branchProfileEntity.inputFile.fileName : ''}</dd>
          <dt>Network</dt>
          <dd>{branchProfileEntity.network ? branchProfileEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/branch-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/branch-profile/${branchProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BranchProfileDetail;
