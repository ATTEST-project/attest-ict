import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGeneratorExtension } from 'app/shared/model/generator-extension.model';
import { getEntities as getGeneratorExtensions } from 'app/entities/generator-extension/generator-extension.reducer';
import { IGenTag } from 'app/shared/model/gen-tag.model';
import { getEntities as getGenTags } from 'app/entities/gen-tag/gen-tag.reducer';
import { IGenCost } from 'app/shared/model/gen-cost.model';
import { getEntities as getGenCosts } from 'app/entities/gen-cost/gen-cost.reducer';
import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './generator.reducer';
import { IGenerator } from 'app/shared/model/generator.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { displayButton } from 'app/shared/reducers/back-button-display';

export const GeneratorUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const generatorUrl = props.match.url.replace(/generator(\/.+)$/, 'generator');

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const generatorExtensions = useAppSelector(state => state.generatorExtension.entities);
  const genTags = useAppSelector(state => state.genTag.entities);
  const genCosts = useAppSelector(state => state.genCost.entities);
  const networks = useAppSelector(state => state.network.entities);
  const generatorEntity = useAppSelector(state => state.generator.entity);
  const loading = useAppSelector(state => state.generator.loading);
  const updating = useAppSelector(state => state.generator.updating);
  const updateSuccess = useAppSelector(state => state.generator.updateSuccess);
  const handleClose = () => {
    props.history.push(generatorUrl + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGeneratorExtensions({}));
    dispatch(getGenTags({}));
    dispatch(getGenCosts({}));
    dispatch(getNetworks({}));
    dispatch(displayButton(false));
    return () => {
      dispatch(displayButton(true));
    };
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...generatorEntity,
      ...values,
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
      ? {}
      : {
          ...generatorEntity,
          network: generatorEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.generator.home.createOrEditLabel" data-cy="GeneratorCreateUpdateHeading">
            Create or edit a Generator
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="generator-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Bus Num" id="generator-busNum" name="busNum" data-cy="busNum" type="text" />
              <ValidatedField label="Pg" id="generator-pg" name="pg" data-cy="pg" type="text" />
              <ValidatedField label="Qg" id="generator-qg" name="qg" data-cy="qg" type="text" />
              <ValidatedField label="Qmax" id="generator-qmax" name="qmax" data-cy="qmax" type="text" />
              <ValidatedField label="Qmin" id="generator-qmin" name="qmin" data-cy="qmin" type="text" />
              <ValidatedField label="Vg" id="generator-vg" name="vg" data-cy="vg" type="text" />
              <ValidatedField label="M Base" id="generator-mBase" name="mBase" data-cy="mBase" type="text" />
              <ValidatedField label="Status" id="generator-status" name="status" data-cy="status" type="text" />
              <ValidatedField label="Pmax" id="generator-pmax" name="pmax" data-cy="pmax" type="text" />
              <ValidatedField label="Pmin" id="generator-pmin" name="pmin" data-cy="pmin" type="text" />
              <ValidatedField label="Pc 1" id="generator-pc1" name="pc1" data-cy="pc1" type="text" />
              <ValidatedField label="Pc 2" id="generator-pc2" name="pc2" data-cy="pc2" type="text" />
              <ValidatedField label="Qc 1 Min" id="generator-qc1min" name="qc1min" data-cy="qc1min" type="text" />
              <ValidatedField label="Qc 1 Max" id="generator-qc1max" name="qc1max" data-cy="qc1max" type="text" />
              <ValidatedField label="Qc 2 Min" id="generator-qc2min" name="qc2min" data-cy="qc2min" type="text" />
              <ValidatedField label="Qc 2 Max" id="generator-qc2max" name="qc2max" data-cy="qc2max" type="text" />
              <ValidatedField label="Ramp Agc" id="generator-rampAgc" name="rampAgc" data-cy="rampAgc" type="text" />
              <ValidatedField label="Ramp 10" id="generator-ramp10" name="ramp10" data-cy="ramp10" type="text" />
              <ValidatedField label="Ramp 30" id="generator-ramp30" name="ramp30" data-cy="ramp30" type="text" />
              <ValidatedField label="Ramp Q" id="generator-rampQ" name="rampQ" data-cy="rampQ" type="text" />
              <ValidatedField label="Apf" id="generator-apf" name="apf" data-cy="apf" type="text" />
              <ValidatedField id="generator-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to={generatorUrl} replace color="info">
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

export default GeneratorUpdate;
