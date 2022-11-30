import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IInputFile } from 'app/shared/model/input-file.model';
import { getEntities as getInputFiles } from 'app/entities/input-file/input-file.reducer';
import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './load-profile.reducer';
import { ILoadProfile } from 'app/shared/model/load-profile.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LoadProfileUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const inputFiles = useAppSelector(state => state.inputFile.entities);
  const networks = useAppSelector(state => state.network.entities);
  const loadProfileEntity = useAppSelector(state => state.loadProfile.entity);
  const loading = useAppSelector(state => state.loadProfile.loading);
  const updating = useAppSelector(state => state.loadProfile.updating);
  const updateSuccess = useAppSelector(state => state.loadProfile.updateSuccess);
  const handleClose = () => {
    props.history.push('/load-profile' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getInputFiles({}));
    dispatch(getNetworks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.uploadDateTime = convertDateTimeToServer(values.uploadDateTime);

    const entity = {
      ...loadProfileEntity,
      ...values,
      inputFile: inputFiles.find(it => it.id.toString() === values.inputFile.toString()),
      network: networks.find(it => it.id.toString() === values.network.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          uploadDateTime: displayDefaultDateTime(),
        }
      : {
          ...loadProfileEntity,
          uploadDateTime: convertDateTimeFromServer(loadProfileEntity.uploadDateTime),
          inputFile: loadProfileEntity?.inputFile?.id,
          network: loadProfileEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.loadProfile.home.createOrEditLabel" data-cy="LoadProfileCreateUpdateHeading">
            Create or edit a LoadProfile
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="load-profile-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Season" id="load-profile-season" name="season" data-cy="season" type="text" />
              <ValidatedField label="Typical Day" id="load-profile-typicalDay" name="typicalDay" data-cy="typicalDay" type="text" />
              <ValidatedField label="Mode" id="load-profile-mode" name="mode" data-cy="mode" type="text" />
              <ValidatedField label="Time Interval" id="load-profile-timeInterval" name="timeInterval" data-cy="timeInterval" type="text" />
              <ValidatedField
                label="Upload Date Time"
                id="load-profile-uploadDateTime"
                name="uploadDateTime"
                data-cy="uploadDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="load-profile-inputFile" name="inputFile" data-cy="inputFile" label="Input File" type="select">
                <option value="" key="0" />
                {inputFiles
                  ? inputFiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.fileName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="load-profile-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/load-profile" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default LoadProfileUpdate;
