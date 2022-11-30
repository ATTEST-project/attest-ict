import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './transf-profile.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransfProfileDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const transfProfileEntity = useAppSelector(state => state.transfProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transfProfileDetailsHeading">TransfProfile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{transfProfileEntity.id}</dd>
          <dt>
            <span id="season">Season</span>
          </dt>
          <dd>{transfProfileEntity.season}</dd>
          <dt>
            <span id="typicalDay">Typical Day</span>
          </dt>
          <dd>{transfProfileEntity.typicalDay}</dd>
          <dt>
            <span id="mode">Mode</span>
          </dt>
          <dd>{transfProfileEntity.mode}</dd>
          <dt>
            <span id="timeInterval">Time Interval</span>
          </dt>
          <dd>{transfProfileEntity.timeInterval}</dd>
          <dt>
            <span id="uploadDateTime">Upload Date Time</span>
          </dt>
          <dd>
            {transfProfileEntity.uploadDateTime ? (
              <TextFormat value={transfProfileEntity.uploadDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Input File</dt>
          <dd>{transfProfileEntity.inputFile ? transfProfileEntity.inputFile.fileName : ''}</dd>
          <dt>Network</dt>
          <dd>{transfProfileEntity.network ? transfProfileEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/transf-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transf-profile/${transfProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransfProfileDetail;
