import React, { useEffect } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { TextFormat } from 'react-jhipster';
import NetworkInfo from 'app/shared/components/network-info/network-info';
import { getEntity } from 'app/entities/network/network.reducer';
import { getEntitiesByNetworkId } from 'app/entities/input-file/input-file.reducer';

import NetworkUploadDX from 'app/modules/network/upload/type/distribution/network-upload-dx';
import NetworkUploadMatpower from 'app/modules/network/upload/type/matpower/network-upload-matpower';
import NetworkUploadTX from 'app/modules/network/upload/type/transmission/network-upload-tx';
import Divider from 'app/shared/components/divider/divider';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { SECTION } from 'app/shared/util/file-utils';

export const NetworkUpload = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [networkEntity, setNetworkEntity] = React.useState(null);
  const [inputFile, setInputFile] = React.useState(null);
  const [isFetchInputFilesFulfilled, setFetchInputFilesFulfilled] = React.useState<boolean>(false);
  const [isHideSld, setHideSld] = React.useState<boolean>(true);

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
    // console.log('NetworkUpload - setInputFileCallback()  ', file);
    setInputFile(file);
    setHideSld(false);
  };

  const handleGoBack = () => {
    props.history.push('/network');
  };

  return (
    <>
      {networkEntity && isFetchInputFilesFulfilled && (
        <>
          <h4>
            <FontAwesomeIcon icon="file-upload" /> Upload Network Data{' '}
          </h4>
          <Divider />
          <NetworkInfo network={networkEntity} hideSld={isHideSld} />
          <Divider />

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
