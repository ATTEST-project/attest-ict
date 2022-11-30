import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IInputFile } from 'app/shared/model/input-file.model';
import { getEntities as getInputFiles } from 'app/entities/input-file/input-file.reducer';
import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './gen-profile.reducer';
import { IGenProfile } from 'app/shared/model/gen-profile.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenProfileUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const inputFiles = useAppSelector(state => state.inputFile.entities);
  const networks = useAppSelector(state => state.network.entities);
  const genProfileEntity = useAppSelector(state => state.genProfile.entity);
  const loading = useAppSelector(state => state.genProfile.loading);
  const updating = useAppSelector(state => state.genProfile.updating);
  const updateSuccess = useAppSelector(state => state.genProfile.updateSuccess);
  const handleClose = () => {
    props.history.push('/gen-profile' + props.location.search);
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
      ...genProfileEntity,
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
          ...genProfileEntity,
          uploadDateTime: convertDateTimeFromServer(genProfileEntity.uploadDateTime),
          inputFile: genProfileEntity?.inputFile?.id,
          network: genProfileEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.genProfile.home.createOrEditLabel" data-cy="GenProfileCreateUpdateHeading">
            Create or edit a GenProfile
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="gen-profile-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Season" id="gen-profile-season" name="season" data-cy="season" type="text" />
              <ValidatedField label="Typical Day" id="gen-profile-typicalDay" name="typicalDay" data-cy="typicalDay" type="text" />
              <ValidatedField label="Mode" id="gen-profile-mode" name="mode" data-cy="mode" type="text" />
              <ValidatedField label="Time Interval" id="gen-profile-timeInterval" name="timeInterval" data-cy="timeInterval" type="text" />
              <ValidatedField
                label="Upload Date Time"
                id="gen-profile-uploadDateTime"
                name="uploadDateTime"
                data-cy="uploadDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="gen-profile-inputFile" name="inputFile" data-cy="inputFile" label="Input File" type="select">
                <option value="" key="0" />
                {inputFiles
                  ? inputFiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.fileName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="gen-profile-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/gen-profile" replace color="info">
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

export default GenProfileUpdate;
