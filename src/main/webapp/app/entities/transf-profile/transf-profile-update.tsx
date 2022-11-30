import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IInputFile } from 'app/shared/model/input-file.model';
import { getEntities as getInputFiles } from 'app/entities/input-file/input-file.reducer';
import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './transf-profile.reducer';
import { ITransfProfile } from 'app/shared/model/transf-profile.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransfProfileUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const inputFiles = useAppSelector(state => state.inputFile.entities);
  const networks = useAppSelector(state => state.network.entities);
  const transfProfileEntity = useAppSelector(state => state.transfProfile.entity);
  const loading = useAppSelector(state => state.transfProfile.loading);
  const updating = useAppSelector(state => state.transfProfile.updating);
  const updateSuccess = useAppSelector(state => state.transfProfile.updateSuccess);
  const handleClose = () => {
    props.history.push('/transf-profile' + props.location.search);
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
      ...transfProfileEntity,
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
          ...transfProfileEntity,
          uploadDateTime: convertDateTimeFromServer(transfProfileEntity.uploadDateTime),
          inputFile: transfProfileEntity?.inputFile?.id,
          network: transfProfileEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.transfProfile.home.createOrEditLabel" data-cy="TransfProfileCreateUpdateHeading">
            Create or edit a TransfProfile
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="transf-profile-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Season" id="transf-profile-season" name="season" data-cy="season" type="text" />
              <ValidatedField label="Typical Day" id="transf-profile-typicalDay" name="typicalDay" data-cy="typicalDay" type="text" />
              <ValidatedField label="Mode" id="transf-profile-mode" name="mode" data-cy="mode" type="text" />
              <ValidatedField
                label="Time Interval"
                id="transf-profile-timeInterval"
                name="timeInterval"
                data-cy="timeInterval"
                type="text"
              />
              <ValidatedField
                label="Upload Date Time"
                id="transf-profile-uploadDateTime"
                name="uploadDateTime"
                data-cy="uploadDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="transf-profile-inputFile" name="inputFile" data-cy="inputFile" label="Input File" type="select">
                <option value="" key="0" />
                {inputFiles
                  ? inputFiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.fileName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="transf-profile-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transf-profile" replace color="info">
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

export default TransfProfileUpdate;
