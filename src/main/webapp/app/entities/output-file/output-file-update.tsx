import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITool } from 'app/shared/model/tool.model';
import { getEntities as getTools } from 'app/entities/tool/tool.reducer';
import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { ISimulation } from 'app/shared/model/simulation.model';
import { getEntities as getSimulations } from 'app/entities/simulation/simulation.reducer';
import { getEntity, updateEntity, createEntity, reset } from './output-file.reducer';
import { IOutputFile } from 'app/shared/model/output-file.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OutputFileUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tools = useAppSelector(state => state.tool.entities);
  const networks = useAppSelector(state => state.network.entities);
  const simulations = useAppSelector(state => state.simulation.entities);
  const outputFileEntity = useAppSelector(state => state.outputFile.entity);
  const loading = useAppSelector(state => state.outputFile.loading);
  const updating = useAppSelector(state => state.outputFile.updating);
  const updateSuccess = useAppSelector(state => state.outputFile.updateSuccess);
  const handleClose = () => {
    props.history.push('/output-file' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTools({}));
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
      ...outputFileEntity,
      ...values,
      tool: tools.find(it => it.id.toString() === values.tool.toString()),
      network: networks.find(it => it.id.toString() === values.network.toString()),
      simulation: simulations.find(it => it.id.toString() === values.simulation.toString()),
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
          ...outputFileEntity,
          uploadTime: convertDateTimeFromServer(outputFileEntity.uploadTime),
          tool: outputFileEntity?.tool?.id,
          network: outputFileEntity?.network?.id,
          simulation: outputFileEntity?.simulation?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.outputFile.home.createOrEditLabel" data-cy="OutputFileCreateUpdateHeading">
            Create or edit a OutputFile
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="output-file-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="File Name" id="output-file-fileName" name="fileName" data-cy="fileName" type="text" />
              <ValidatedField label="Description" id="output-file-description" name="description" data-cy="description" type="text" />
              <ValidatedBlobField label="Data" id="output-file-data" name="data" data-cy="data" openActionLabel="Open" />
              <ValidatedField
                label="Upload Time"
                id="output-file-uploadTime"
                name="uploadTime"
                data-cy="uploadTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="output-file-tool" name="tool" data-cy="tool" label="Tool" type="select">
                <option value="" key="0" />
                {tools
                  ? tools.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="output-file-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="output-file-simulation" name="simulation" data-cy="simulation" label="Simulation" type="select">
                <option value="" key="0" />
                {simulations
                  ? simulations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.uuid}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/output-file" replace color="info">
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

export default OutputFileUpdate;
