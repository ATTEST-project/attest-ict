import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './branch-el-val.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BranchElValDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const branchElValEntity = useAppSelector(state => state.branchElVal.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="branchElValDetailsHeading">BranchElVal</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{branchElValEntity.id}</dd>
          <dt>
            <span id="hour">Hour</span>
          </dt>
          <dd>{branchElValEntity.hour}</dd>
          <dt>
            <span id="min">Min</span>
          </dt>
          <dd>{branchElValEntity.min}</dd>
          <dt>
            <span id="p">P</span>
          </dt>
          <dd>{branchElValEntity.p}</dd>
          <dt>
            <span id="q">Q</span>
          </dt>
          <dd>{branchElValEntity.q}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{branchElValEntity.status}</dd>
          <dt>
            <span id="branchIdOnSubst">Branch Id On Subst</span>
          </dt>
          <dd>{branchElValEntity.branchIdOnSubst}</dd>
          <dt>
            <span id="nominalVoltage">Nominal Voltage</span>
          </dt>
          <dd>{branchElValEntity.nominalVoltage}</dd>
          <dt>Branch</dt>
          <dd>{branchElValEntity.branch ? branchElValEntity.branch.id : ''}</dd>
          <dt>Branch Profile</dt>
          <dd>{branchElValEntity.branchProfile ? branchElValEntity.branchProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/branch-el-val" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/branch-el-val/${branchElValEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BranchElValDetail;
