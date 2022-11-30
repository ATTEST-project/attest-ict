import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './flex-el-val.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FlexElValDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const flexElValEntity = useAppSelector(state => state.flexElVal.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="flexElValDetailsHeading">FlexElVal</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{flexElValEntity.id}</dd>
          <dt>
            <span id="busNum">Bus Num</span>
          </dt>
          <dd>{flexElValEntity.busNum}</dd>
          <dt>
            <span id="hour">Hour</span>
          </dt>
          <dd>{flexElValEntity.hour}</dd>
          <dt>
            <span id="min">Min</span>
          </dt>
          <dd>{flexElValEntity.min}</dd>
          <dt>
            <span id="pfmaxUp">Pfmax Up</span>
          </dt>
          <dd>{flexElValEntity.pfmaxUp}</dd>
          <dt>
            <span id="pfmaxDn">Pfmax Dn</span>
          </dt>
          <dd>{flexElValEntity.pfmaxDn}</dd>
          <dt>
            <span id="qfmaxUp">Qfmax Up</span>
          </dt>
          <dd>{flexElValEntity.qfmaxUp}</dd>
          <dt>
            <span id="qfmaxDn">Qfmax Dn</span>
          </dt>
          <dd>{flexElValEntity.qfmaxDn}</dd>
          <dt>Flex Profile</dt>
          <dd>{flexElValEntity.flexProfile ? flexElValEntity.flexProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/flex-el-val" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/flex-el-val/${flexElValEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FlexElValDetail;
