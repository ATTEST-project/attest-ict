import React, { useEffect } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/network/network.reducer';
import NetworkUploadDX from 'app/modules/network/upload/type/distribution/network-upload-dx';
import NetworkUploadMatpower from 'app/modules/network/upload/type/matpower/network-upload-matpower';
import NetworkUploadTX from 'app/modules/network/upload/type/transmission/network-upload-tx';
import { getEntitiesByNetworkId } from 'app/entities/input-file/input-file.reducer';

export const NetworkUpload = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  // const networkEntity = useAppSelector(state => state.network.entity)
  const [networkEntity, setNetworkEntity] = React.useState(null);
  const [matpowerFile, setMatpowerFile] = React.useState(null);

  useEffect(() => {
    dispatch(getEntity(props.match.params.id))
      .unwrap()
      .then(res => setNetworkEntity(res.data))
      .catch(err => setNetworkEntity(null));
    getMatpowerFileUploaded();
  }, []);

  const getMatpowerFileUploaded = () => {
    dispatch(getEntitiesByNetworkId(props.match.params.id))
      .unwrap()
      .then(res => {
        const fileFound = res.data.find(file => file.description === 'matpower');
        if (fileFound) {
          setMatpowerFile(fileFound);
        }
      });
  };

  const setMatpowerFileCallback = file => {
    setMatpowerFile(file);
  };

  const handleGoBack = () => {
    props.history.push('/network');
  };

  return (
    <>
      {networkEntity && (
        <>
          <h4>{'Upload data for network ' + networkEntity.name}</h4>
          <NetworkUploadMatpower network={networkEntity} callback={setMatpowerFileCallback} />
          {matpowerFile &&
            (networkEntity.type && networkEntity.type.toLowerCase() === 'dx' ? (
              <NetworkUploadDX network={networkEntity} />
            ) : (
              <NetworkUploadTX network={networkEntity} />
            ))}
        </>
      )}
      <Row>
        <Col md="8">
          <Button onClick={handleGoBack} color="info" data-cy="entityUploadBackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
        </Col>
      </Row>
    </>
  );
};

export default NetworkUpload;
