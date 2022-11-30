import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGenerator } from 'app/shared/model/generator.model';
import { getEntities as getGenerators } from 'app/entities/generator/generator.reducer';
import { getEntity, updateEntity, createEntity, reset } from './gen-cost.reducer';
import { IGenCost } from 'app/shared/model/gen-cost.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenCostUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const generators = useAppSelector(state => state.generator.entities);
  const genCostEntity = useAppSelector(state => state.genCost.entity);
  const loading = useAppSelector(state => state.genCost.loading);
  const updating = useAppSelector(state => state.genCost.updating);
  const updateSuccess = useAppSelector(state => state.genCost.updateSuccess);
  const handleClose = () => {
    props.history.push('/gen-cost' + props.location.search);
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
      ...genCostEntity,
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
          ...genCostEntity,
          generator: genCostEntity?.generator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.genCost.home.createOrEditLabel" data-cy="GenCostCreateUpdateHeading">
            Create or edit a GenCost
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="gen-cost-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Model" id="gen-cost-model" name="model" data-cy="model" type="text" />
              <ValidatedField label="Startup" id="gen-cost-startup" name="startup" data-cy="startup" type="text" />
              <ValidatedField label="Shutdown" id="gen-cost-shutdown" name="shutdown" data-cy="shutdown" type="text" />
              <ValidatedField label="N Cost" id="gen-cost-nCost" name="nCost" data-cy="nCost" type="text" />
              <ValidatedField label="Cost PF" id="gen-cost-costPF" name="costPF" data-cy="costPF" type="text" />
              <ValidatedField label="Cost QF" id="gen-cost-costQF" name="costQF" data-cy="costQF" type="text" />
              <ValidatedField id="gen-cost-generator" name="generator" data-cy="generator" label="Generator" type="select">
                <option value="" key="0" />
                {generators
                  ? generators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.busNum}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/gen-cost" replace color="info">
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

export default GenCostUpdate;
