import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITool } from 'app/shared/model/tool.model';
import { getEntities as getTools } from 'app/entities/tool/tool.reducer';
import { IGenProfile } from 'app/shared/model/gen-profile.model';
import { getEntities as getGenProfiles } from 'app/entities/gen-profile/gen-profile.reducer';
import { IFlexProfile } from 'app/shared/model/flex-profile.model';
import { getEntities as getFlexProfiles } from 'app/entities/flex-profile/flex-profile.reducer';
import { ILoadProfile } from 'app/shared/model/load-profile.model';
import { getEntities as getLoadProfiles } from 'app/entities/load-profile/load-profile.reducer';
import { ITransfProfile } from 'app/shared/model/transf-profile.model';
import { getEntities as getTransfProfiles } from 'app/entities/transf-profile/transf-profile.reducer';
import { IBranchProfile } from 'app/shared/model/branch-profile.model';
import { getEntities as getBranchProfiles } from 'app/entities/branch-profile/branch-profile.reducer';
import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { ISimulation } from 'app/shared/model/simulation.model';
import { getEntities as getSimulations } from 'app/entities/simulation/simulation.reducer';
import { getEntity, updateEntity, createEntity, reset } from './input-file.reducer';
import { IInputFile } from 'app/shared/model/input-file.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const InputFileUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tools = useAppSelector(state => state.tool.entities);
  const genProfiles = useAppSelector(state => state.genProfile.entities);
  const flexProfiles = useAppSelector(state => state.flexProfile.entities);
  const loadProfiles = useAppSelector(state => state.loadProfile.entities);
  const transfProfiles = useAppSelector(state => state.transfProfile.entities);
  const branchProfiles = useAppSelector(state => state.branchProfile.entities);
  const networks = useAppSelector(state => state.network.entities);
  const simulations = useAppSelector(state => state.simulation.entities);
  const inputFileEntity = useAppSelector(state => state.inputFile.entity);
  const loading = useAppSelector(state => state.inputFile.loading);
  const updating = useAppSelector(state => state.inputFile.updating);
  const updateSuccess = useAppSelector(state => state.inputFile.updateSuccess);
  const handleClose = () => {
    props.history.push('/input-file' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTools({}));
    dispatch(getGenProfiles({}));
    dispatch(getFlexProfiles({}));
    dispatch(getLoadProfiles({}));
    dispatch(getTransfProfiles({}));
    dispatch(getBranchProfiles({}));
    dispatch(getNetworks({}));
    dispatch(getSimulations({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.uploadTime = convertDateTimeToServer(values.uploadTime);

    const entity = {
      ...inputFileEntity,
      ...values,
      tool: tools.find(it => it.id.toString() === values.tool.toString()),
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
          uploadTime: displayDefaultDateTime(),
        }
      : {
          ...inputFileEntity,
          uploadTime: convertDateTimeFromServer(inputFileEntity.uploadTime),
          tool: inputFileEntity?.tool?.id,
          network: inputFileEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.inputFile.home.createOrEditLabel" data-cy="InputFileCreateUpdateHeading">
            Create or edit a InputFile
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="input-file-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="File Name" id="input-file-fileName" name="fileName" data-cy="fileName" type="text" />
              <ValidatedField label="Description" id="input-file-description" name="description" data-cy="description" type="text" />
              <ValidatedBlobField label="Data" id="input-file-data" name="data" data-cy="data" openActionLabel="Open" />
              <ValidatedField
                label="Upload Time"
                id="input-file-uploadTime"
                name="uploadTime"
                data-cy="uploadTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="input-file-tool" name="tool" data-cy="tool" label="Tool" type="select">
                <option value="" key="0" />
                {tools
                  ? tools.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="input-file-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/input-file" replace color="info">
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

export default InputFileUpdate;
