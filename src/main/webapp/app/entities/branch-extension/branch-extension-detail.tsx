import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './branch-extension.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BranchExtensionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const branchExtensionEntity = useAppSelector(state => state.branchExtension.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="branchExtensionDetailsHeading">BranchExtension</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{branchExtensionEntity.id}</dd>
          <dt>
            <span id="stepSize">Step Size</span>
          </dt>
          <dd>{branchExtensionEntity.stepSize}</dd>
          <dt>
            <span id="actTap">Act Tap</span>
          </dt>
          <dd>{branchExtensionEntity.actTap}</dd>
          <dt>
            <span id="minTap">Min Tap</span>
          </dt>
          <dd>{branchExtensionEntity.minTap}</dd>
          <dt>
            <span id="maxTap">Max Tap</span>
          </dt>
          <dd>{branchExtensionEntity.maxTap}</dd>
          <dt>
            <span id="normalTap">Normal Tap</span>
          </dt>
          <dd>{branchExtensionEntity.normalTap}</dd>
          <dt>
            <span id="nominalRatio">Nominal Ratio</span>
          </dt>
          <dd>{branchExtensionEntity.nominalRatio}</dd>
          <dt>
            <span id="rIp">R Ip</span>
          </dt>
          <dd>{branchExtensionEntity.rIp}</dd>
          <dt>
            <span id="rN">R N</span>
          </dt>
          <dd>{branchExtensionEntity.rN}</dd>
          <dt>
            <span id="r0">R 0</span>
          </dt>
          <dd>{branchExtensionEntity.r0}</dd>
          <dt>
            <span id="x0">X 0</span>
          </dt>
          <dd>{branchExtensionEntity.x0}</dd>
          <dt>
            <span id="b0">B 0</span>
          </dt>
          <dd>{branchExtensionEntity.b0}</dd>
          <dt>
            <span id="length">Length</span>
          </dt>
          <dd>{branchExtensionEntity.length}</dd>
          <dt>
            <span id="normStat">Norm Stat</span>
          </dt>
          <dd>{branchExtensionEntity.normStat}</dd>
          <dt>
            <span id="g">G</span>
          </dt>
          <dd>{branchExtensionEntity.g}</dd>
          <dt>
            <span id="mRid">M Rid</span>
          </dt>
          <dd>{branchExtensionEntity.mRid}</dd>
          <dt>Branch</dt>
          <dd>{branchExtensionEntity.branch ? branchExtensionEntity.branch.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/branch-extension" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/branch-extension/${branchExtensionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BranchExtensionDetail;
