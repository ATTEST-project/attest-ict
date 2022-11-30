import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGenerator } from 'app/shared/model/generator.model';
import { getEntities as getGenerators } from 'app/entities/generator/generator.reducer';
import { getEntity, updateEntity, createEntity, reset } from './generator-extension.reducer';
import { IGeneratorExtension } from 'app/shared/model/generator-extension.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GeneratorExtensionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const generators = useAppSelector(state => state.generator.entities);
  const generatorExtensionEntity = useAppSelector(state => state.generatorExtension.entity);
  const loading = useAppSelector(state => state.generatorExtension.loading);
  const updating = useAppSelector(state => state.generatorExtension.updating);
  const updateSuccess = useAppSelector(state => state.generatorExtension.updateSuccess);
  const handleClose = () => {
    props.history.push('/generator-extension' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGenerators({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...generatorExtensionEntity,
      ...values,
      generator: generators.find(it => it.id.toString() === values.generator.toString()),
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
          ...generatorExtensionEntity,
          generator: generatorExtensionEntity?.generator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.generatorExtension.home.createOrEditLabel" data-cy="GeneratorExtensionCreateUpdateHeading">
            Create or edit a GeneratorExtension
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
                <ValidatedField name="id" required readOnly id="generator-extension-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Id Gen" id="generator-extension-idGen" name="idGen" data-cy="idGen" type="text" />
              <ValidatedField label="Status Curt" id="generator-extension-statusCurt" name="statusCurt" data-cy="statusCurt" type="text" />
              <ValidatedField label="Dg Type" id="generator-extension-dgType" name="dgType" data-cy="dgType" type="text" />
              <ValidatedField id="generator-extension-generator" name="generator" data-cy="generator" label="Generator" type="select">
                <option value="" key="0" />
                {generators
                  ? generators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.busNum}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/generator-extension" replace color="info">
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

export default GeneratorExtensionUpdate;
