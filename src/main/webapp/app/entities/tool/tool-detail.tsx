import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './tool.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ToolDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const toolEntity = useAppSelector(state => state.tool.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="toolDetailsHeading">Tool</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{toolEntity.id}</dd>
          <dt>
            <span id="workPackage">Work Package</span>
          </dt>
          <dd>{toolEntity.workPackage}</dd>
          <dt>
            <span id="num">Num</span>
          </dt>
          <dd>{toolEntity.num}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{toolEntity.name}</dd>
          <dt>
            <span id="path">Path</span>
          </dt>
          <dd>{toolEntity.path}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{toolEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/tool" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tool/${toolEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ToolDetail;
