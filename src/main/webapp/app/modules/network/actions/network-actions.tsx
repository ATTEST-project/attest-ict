import React, { useEffect } from 'react';
import './network-actions.scss';
import { Button, Col, Row, Tooltip } from 'reactstrap';
import { Link, Switch } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/network/network.reducer';
import axios from 'axios';

const NetworkActions = ({ match }) => {
  const dispatch = useAppDispatch();

  const networkEntity = useAppSelector(state => state.network.entity);

  useEffect(() => {
    dispatch(getEntity(match.params.id));
  }, []);

  const [disabled, setDisabled] = React.useState(false);

  React.useEffect(() => {
    if (Object.keys(networkEntity).length === 0) {
      return;
    }
    const requestUrl = '/api/buses/count?networkId.equals=' + networkEntity.id;
    axios
      .get(requestUrl)
      .then(res => setDisabled(res.data === 0))
      .catch(err => {
        throw new Error('Cannot get buses of the network!');
      });
  }, [networkEntity]);

  const ItemGrid = ({ id, url, title, icon, disabled }) => {
    const [show, setShow] = React.useState(false);

    return (
      <>
        <div className="item-grid" onMouseEnter={() => setShow(true)} onMouseLeave={() => setShow(false)}>
          <Button disabled={disabled} id={id} className="item-grid-button" tag={Link} to={url} color="primary">
            <div className="item-grid-button-text">
              <div>
                <span>{title}</span> <FontAwesomeIcon icon={icon} />
              </div>
            </div>
          </Button>
        </div>
        {disabled && (
          <Tooltip isOpen={show} target={id} placement="bottom">
            No data for this network
          </Tooltip>
        )}
      </>
    );
  };

  return (
    <>
      <h4>{'Actions for network ' + networkEntity.name}</h4>
      <Row md="2">
        <Col>
          <ItemGrid id="upload_button" url={`${match.url}/upload`} title={'Upload '} disabled={false} icon="file-upload" />
        </Col>
        <Col>
          <ItemGrid id="export_button" url={`${match.url}/export`} title={'Export '} icon="file-download" disabled={disabled} />
        </Col>
      </Row>
      <Row md="2">
        <Col>
          <ItemGrid id="data_button" url={`${match.url}/data`} title={'Tables '} icon="table" disabled={disabled} />
        </Col>
        <Col>
          <ItemGrid id="sld_button" url={`${match.url}/sld`} title={'Diagram '} icon="project-diagram" disabled={disabled} />
        </Col>
      </Row>
      {/* Comment on 2023/09/05:we are unable to show the network's map, due to the missing data such as coordinates
       <Row md="2">
        <Col>
          <ItemGrid id="map_button" url={`${match.url}/map`} title={'Map '} icon="map" disabled={disabled} />
        </Col>
        <Col />
      </Row>
       */}
      <Row>
        <Col md="8">
          <Button tag={Link} to={'/network'} color="info" data-cy="entityUploadBackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
        </Col>
      </Row>
    </>
  );
};

export default NetworkActions;
