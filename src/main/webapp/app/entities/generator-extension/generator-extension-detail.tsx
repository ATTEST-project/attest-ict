import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './generator-extension.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GeneratorExtensionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const generatorExtensionEntity = useAppSelector(state => state.generatorExtension.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="generatorExtensionDetailsHeading">GeneratorExtension</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{generatorExtensionEntity.id}</dd>
          <dt>
            <span id="idGen">Id Gen</span>
          </dt>
          <dd>{generatorExtensionEntity.idGen}</dd>
          <dt>
            <span id="statusCurt">Status Curt</span>
          </dt>
          <dd>{generatorExtensionEntity.statusCurt}</dd>
          <dt>
            <span id="dgType">Dg Type</span>
          </dt>
          <dd>{generatorExtensionEntity.dgType}</dd>
          <dt>Generator</dt>
          <dd>{generatorExtensionEntity.generator ? generatorExtensionEntity.generator.busNum : ''}</dd>
        </dl>
        <Button tag={Link} to="/generator-extension" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/generator-extension/${generatorExtensionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GeneratorExtensionDetail;
