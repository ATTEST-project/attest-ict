import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './gen-el-val.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenElValDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const genElValEntity = useAppSelector(state => state.genElVal.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="genElValDetailsHeading">GenElVal</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{genElValEntity.id}</dd>
          <dt>
            <span id="hour">Hour</span>
          </dt>
          <dd>{genElValEntity.hour}</dd>
          <dt>
            <span id="min">Min</span>
          </dt>
          <dd>{genElValEntity.min}</dd>
          <dt>
            <span id="p">P</span>
          </dt>
          <dd>{genElValEntity.p}</dd>
          <dt>
            <span id="q">Q</span>
          </dt>
          <dd>{genElValEntity.q}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{genElValEntity.status}</dd>
          <dt>
            <span id="voltageMagnitude">Voltage Magnitude</span>
          </dt>
          <dd>{genElValEntity.voltageMagnitude}</dd>
          <dt>
            <span id="genIdOnSubst">Gen Id On Subst</span>
          </dt>
          <dd>{genElValEntity.genIdOnSubst}</dd>
          <dt>
            <span id="nominalVoltage">Nominal Voltage</span>
          </dt>
          <dd>{genElValEntity.nominalVoltage}</dd>
          <dt>Gen Profile</dt>
          <dd>{genElValEntity.genProfile ? genElValEntity.genProfile.id : ''}</dd>
          <dt>Generator</dt>
          <dd>{genElValEntity.generator ? genElValEntity.generator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/gen-el-val" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/gen-el-val/${genElValEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GenElValDetail;
