import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './generator.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { displayButton } from 'app/shared/reducers/back-button-display';

export const GeneratorDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const generatorUrl = props.match.url.replace(/generator(\/.+)$/, 'generator');

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    dispatch(displayButton(false));
    return () => {
      dispatch(displayButton(true));
    };
  }, []);

  const generatorEntity = useAppSelector(state => state.generator.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="generatorDetailsHeading">Generator</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{generatorEntity.id}</dd>
          <dt>
            <span id="busNum">Bus Num</span>
          </dt>
          <dd>{generatorEntity.busNum}</dd>
          <dt>
            <span id="pg">Pg</span>
          </dt>
          <dd>{generatorEntity.pg}</dd>
          <dt>
            <span id="qg">Qg</span>
          </dt>
          <dd>{generatorEntity.qg}</dd>
          <dt>
            <span id="qmax">Qmax</span>
          </dt>
          <dd>{generatorEntity.qmax}</dd>
          <dt>
            <span id="qmin">Qmin</span>
          </dt>
          <dd>{generatorEntity.qmin}</dd>
          <dt>
            <span id="vg">Vg</span>
          </dt>
          <dd>{generatorEntity.vg}</dd>
          <dt>
            <span id="mBase">M Base</span>
          </dt>
          <dd>{generatorEntity.mBase}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{generatorEntity.status}</dd>
          <dt>
            <span id="pmax">Pmax</span>
          </dt>
          <dd>{generatorEntity.pmax}</dd>
          <dt>
            <span id="pmin">Pmin</span>
          </dt>
          <dd>{generatorEntity.pmin}</dd>
          <dt>
            <span id="pc1">Pc 1</span>
          </dt>
          <dd>{generatorEntity.pc1}</dd>
          <dt>
            <span id="pc2">Pc 2</span>
          </dt>
          <dd>{generatorEntity.pc2}</dd>
          <dt>
            <span id="qc1min">Qc 1 Min</span>
          </dt>
          <dd>{generatorEntity.qc1min}</dd>
          <dt>
            <span id="qc1max">Qc 1 Max</span>
          </dt>
          <dd>{generatorEntity.qc1max}</dd>
          <dt>
            <span id="qc2min">Qc 2 Min</span>
          </dt>
          <dd>{generatorEntity.qc2min}</dd>
          <dt>
            <span id="qc2max">Qc 2 Max</span>
          </dt>
          <dd>{generatorEntity.qc2max}</dd>
          <dt>
            <span id="rampAgc">Ramp Agc</span>
          </dt>
          <dd>{generatorEntity.rampAgc}</dd>
          <dt>
            <span id="ramp10">Ramp 10</span>
          </dt>
          <dd>{generatorEntity.ramp10}</dd>
          <dt>
            <span id="ramp30">Ramp 30</span>
          </dt>
          <dd>{generatorEntity.ramp30}</dd>
          <dt>
            <span id="rampQ">Ramp Q</span>
          </dt>
          <dd>{generatorEntity.rampQ}</dd>
          <dt>
            <span id="apf">Apf</span>
          </dt>
          <dd>{generatorEntity.apf}</dd>
          <dt>Network</dt>
          <dd>{generatorEntity.network ? generatorEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to={generatorUrl} replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`${generatorUrl}/${generatorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GeneratorDetail;
