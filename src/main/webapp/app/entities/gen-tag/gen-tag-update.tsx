import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGenerator } from 'app/shared/model/generator.model';
import { getEntities as getGenerators } from 'app/entities/generator/generator.reducer';
import { getEntity, updateEntity, createEntity, reset } from './gen-tag.reducer';
import { IGenTag } from 'app/shared/model/gen-tag.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenTagUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const generators = useAppSelector(state => state.generator.entities);
  const genTagEntity = useAppSelector(state => state.genTag.entity);
  const loading = useAppSelector(state => state.genTag.loading);
  const updating = useAppSelector(state => state.genTag.updating);
  const updateSuccess = useAppSelector(state => state.genTag.updateSuccess);
  const handleClose = () => {
    props.history.push('/gen-tag' + props.location.search);
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
      ...genTagEntity,
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
          ...genTagEntity,
          generator: genTagEntity?.generator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.genTag.home.createOrEditLabel" data-cy="GenTagCreateUpdateHeading">
            Create or edit a GenTag
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="gen-tag-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Gen Tag" id="gen-tag-genTag" name="genTag" data-cy="genTag" type="text" />
              <ValidatedField id="gen-tag-generator" name="generator" data-cy="generator" label="Generator" type="select">
                <option value="" key="0" />
                {generators
                  ? generators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.busNum}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/gen-tag" replace color="info">
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

export default GenTagUpdate;
