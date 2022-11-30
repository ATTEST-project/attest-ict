import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './gen-tag.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenTagDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const genTagEntity = useAppSelector(state => state.genTag.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="genTagDetailsHeading">GenTag</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{genTagEntity.id}</dd>
          <dt>
            <span id="genTag">Gen Tag</span>
          </dt>
          <dd>{genTagEntity.genTag}</dd>
          <dt>Generator</dt>
          <dd>{genTagEntity.generator ? genTagEntity.generator.busNum : ''}</dd>
        </dl>
        <Button tag={Link} to="/gen-tag" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/gen-tag/${genTagEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GenTagDetail;
