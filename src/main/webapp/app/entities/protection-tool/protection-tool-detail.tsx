import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './protection-tool.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProtectionToolDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const protectionToolEntity = useAppSelector(state => state.protectionTool.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="protectionToolDetailsHeading">ProtectionTool</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{protectionToolEntity.id}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{protectionToolEntity.type}</dd>
          <dt>Branch</dt>
          <dd>{protectionToolEntity.branch ? protectionToolEntity.branch.id : ''}</dd>
          <dt>Bus</dt>
          <dd>{protectionToolEntity.bus ? protectionToolEntity.bus.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/protection-tool" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/protection-tool/${protectionToolEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProtectionToolDetail;
