import React, { useEffect } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from 'app/entities/network/network.reducer';
import { getEntitiesByNetworkId } from 'app/entities/input-file/input-file.reducer';

import NetworkUploadDX from 'app/modules/network/upload/type/distribution/network-upload-dx';
import NetworkUploadMatpower from 'app/modules/network/upload/type/matpower/network-upload-matpower';
import NetworkUploadTX from 'app/modules/network/upload/type/transmission/network-upload-tx';

import { SECTION } from 'app/shared/util/file-utils';

export const NetworkUpload = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [networkEntity, setNetworkEntity] = React.useState(null);
  const [inputFile, setInputFile] = React.useState(null);
  const [isFetchInputFilesFulfilled, setFetchInputFilesFulfilled] = React.useState<boolean>(false);

  const getNetworkFilesUploaded = () => {
    // eslint-disable-next-line no-console
    console.log('NetworkUpload - getNetworkFileUploaded(),  network_id ', props.match.params.id);
    dispatch(getEntitiesByNetworkId(props.match.params.id))
      .unwrap()
      .then(res => {
        setFetchInputFilesFulfilled(true); // true when inputFile/fetch_entity_list is fullfilled
      })
      .catch(err => {
        // eslint-disable-next-line no-console
        console.error('NetworkUpload - getNetworkFileUploaded(), unable to find file for network_id: ', props.match.params.id);
        setInputFile(null);
        setFetchInputFilesFulfilled(false);
      });
  };

  useEffect(() => {
    // eslint-disable-next-line no-console
    console.log('NetworkUpload - useEffect(), network id: ', props.match.params.id);
    dispatch(getEntity(props.match.params.id))
      .unwrap()
      .then(res => {
        setNetworkEntity(res.data);
        getNetworkFilesUploaded();
      })
      .catch(err => {
        // eslint-disable-next-line no-console
        console.error('NetworkUpload - useEffect() unable to find Network by id ', props.match.params.id);
        setNetworkEntity(null);
        setInputFile(null);
        setFetchInputFilesFulfilled(false);
      });
  }, [props.match.params.id]);

  const setInputFileCallback = file => {
    // eslint-disable-next-line no-console
    console.log('NetworkUpload - setInputFileCallback()  ', file);
    setInputFile(file);
  };

  const handleGoBack = () => {
    props.history.push('/network');
  };

  return (
    <>
      {networkEntity && isFetchInputFilesFulfilled && (
        <>
          <h4>{'Upload data for network: ' + networkEntity.name}</h4>
          <NetworkUploadMatpower network={networkEntity} callback={setInputFileCallback} />
          {inputFile &&
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
