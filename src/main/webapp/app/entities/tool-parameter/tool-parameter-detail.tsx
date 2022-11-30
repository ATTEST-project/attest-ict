import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './tool-parameter.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ToolParameterDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const toolParameterEntity = useAppSelector(state => state.toolParameter.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="toolParameterDetailsHeading">ToolParameter</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{toolParameterEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{toolParameterEntity.name}</dd>
          <dt>
            <span id="defaultValue">Default Value</span>
          </dt>
          <dd>{toolParameterEntity.defaultValue}</dd>
          <dt>
            <span id="isEnabled">Is Enabled</span>
          </dt>
          <dd>{toolParameterEntity.isEnabled ? 'true' : 'false'}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{toolParameterEntity.description}</dd>
          <dt>
            <span id="lastUpdate">Last Update</span>
          </dt>
          <dd>
            {toolParameterEntity.lastUpdate ? (
              <TextFormat value={toolParameterEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Tool</dt>
          <dd>{toolParameterEntity.tool ? toolParameterEntity.tool.num : ''}</dd>
        </dl>
        <Button tag={Link} to="/tool-parameter" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tool-parameter/${toolParameterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ToolParameterDetail;
