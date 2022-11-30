import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITool } from 'app/shared/model/tool.model';
import { getEntities as getTools } from 'app/entities/tool/tool.reducer';
import { getEntity, updateEntity, createEntity, reset } from './tool-parameter.reducer';
import { IToolParameter } from 'app/shared/model/tool-parameter.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ToolParameterUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tools = useAppSelector(state => state.tool.entities);
  const toolParameterEntity = useAppSelector(state => state.toolParameter.entity);
  const loading = useAppSelector(state => state.toolParameter.loading);
  const updating = useAppSelector(state => state.toolParameter.updating);
  const updateSuccess = useAppSelector(state => state.toolParameter.updateSuccess);
  const handleClose = () => {
    props.history.push('/tool-parameter' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTools({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.lastUpdate = convertDateTimeToServer(values.lastUpdate);

    const entity = {
      ...toolParameterEntity,
      ...values,
      tool: tools.find(it => it.id.toString() === values.tool.toString()),
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
          lastUpdate: displayDefaultDateTime(),
        }
      : {
          ...toolParameterEntity,
          lastUpdate: convertDateTimeFromServer(toolParameterEntity.lastUpdate),
          tool: toolParameterEntity?.tool?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.toolParameter.home.createOrEditLabel" data-cy="ToolParameterCreateUpdateHeading">
            Create or edit a ToolParameter
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
                <ValidatedField name="id" required readOnly id="tool-parameter-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Name"
                id="tool-parameter-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Default Value"
                id="tool-parameter-defaultValue"
                name="defaultValue"
                data-cy="defaultValue"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Is Enabled" id="tool-parameter-isEnabled" name="isEnabled" data-cy="isEnabled" check type="checkbox" />
              <ValidatedField label="Description" id="tool-parameter-description" name="description" data-cy="description" type="text" />
              <ValidatedField
                label="Last Update"
                id="tool-parameter-lastUpdate"
                name="lastUpdate"
                data-cy="lastUpdate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="tool-parameter-tool" name="tool" data-cy="tool" label="Tool" type="select">
                <option value="" key="0" />
                {tools
                  ? tools.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.num}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tool-parameter" replace color="info">
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

export default ToolParameterUpdate;
