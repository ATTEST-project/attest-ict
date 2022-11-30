import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './price.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PriceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const priceEntity = useAppSelector(state => state.price.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="priceDetailsHeading">Price</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{priceEntity.id}</dd>
          <dt>
            <span id="electricityEnergy">Electricity Energy</span>
          </dt>
          <dd>{priceEntity.electricityEnergy}</dd>
          <dt>
            <span id="gasEnergy">Gas Energy</span>
          </dt>
          <dd>{priceEntity.gasEnergy}</dd>
          <dt>
            <span id="secondaryBand">Secondary Band</span>
          </dt>
          <dd>{priceEntity.secondaryBand}</dd>
          <dt>
            <span id="secondaryUp">Secondary Up</span>
          </dt>
          <dd>{priceEntity.secondaryUp}</dd>
          <dt>
            <span id="secondaryDown">Secondary Down</span>
          </dt>
          <dd>{priceEntity.secondaryDown}</dd>
          <dt>
            <span id="secondaryRatioUp">Secondary Ratio Up</span>
          </dt>
          <dd>{priceEntity.secondaryRatioUp}</dd>
          <dt>
            <span id="secondaryRatioDown">Secondary Ratio Down</span>
          </dt>
          <dd>{priceEntity.secondaryRatioDown}</dd>
        </dl>
        <Button tag={Link} to="/price" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/price/${priceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PriceDetail;
