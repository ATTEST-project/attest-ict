import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './branch.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { displayButton } from 'app/shared/reducers/back-button-display';

export const BranchDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const branchUrl = props.match.url.replace(/branch(\/.+)$/, 'branch');

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    dispatch(displayButton(false));
    return () => {
      dispatch(displayButton(true));
    };
  }, []);

  const branchEntity = useAppSelector(state => state.branch.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="branchDetailsHeading">Branch</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{branchEntity.id}</dd>
          <dt>
            <span id="fbus">Fbus</span>
          </dt>
          <dd>{branchEntity.fbus}</dd>
          <dt>
            <span id="tbus">Tbus</span>
          </dt>
          <dd>{branchEntity.tbus}</dd>
          <dt>
            <span id="r">R</span>
          </dt>
          <dd>{branchEntity.r}</dd>
          <dt>
            <span id="x">X</span>
          </dt>
          <dd>{branchEntity.x}</dd>
          <dt>
            <span id="b">B</span>
          </dt>
          <dd>{branchEntity.b}</dd>
          <dt>
            <span id="ratea">Ratea</span>
          </dt>
          <dd>{branchEntity.ratea}</dd>
          <dt>
            <span id="rateb">Rateb</span>
          </dt>
          <dd>{branchEntity.rateb}</dd>
          <dt>
            <span id="ratec">Ratec</span>
          </dt>
          <dd>{branchEntity.ratec}</dd>
          <dt>
            <span id="tapRatio">Tap Ratio</span>
          </dt>
          <dd>{branchEntity.tapRatio}</dd>
          <dt>
            <span id="angle">Angle</span>
          </dt>
          <dd>{branchEntity.angle}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{branchEntity.status}</dd>
          <dt>
            <span id="angmin">Angmin</span>
          </dt>
          <dd>{branchEntity.angmin}</dd>
          <dt>
            <span id="angmax">Angmax</span>
          </dt>
          <dd>{branchEntity.angmax}</dd>
          <dt>Network</dt>
          <dd>{branchEntity.network ? branchEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to={branchUrl} replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`${branchUrl}/${branchEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BranchDetail;
