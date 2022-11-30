import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITopologyBus } from 'app/shared/model/topology-bus.model';
import { getEntities as getTopologyBuses } from 'app/entities/topology-bus/topology-bus.reducer';
import { getEntity, updateEntity, createEntity, reset } from './topology.reducer';
import { ITopology } from 'app/shared/model/topology.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TopologyUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const topologyBuses = useAppSelector(state => state.topologyBus.entities);
  const topologyEntity = useAppSelector(state => state.topology.entity);
  const loading = useAppSelector(state => state.topology.loading);
  const updating = useAppSelector(state => state.topology.updating);
  const updateSuccess = useAppSelector(state => state.topology.updateSuccess);
  const handleClose = () => {
    props.history.push('/topology' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTopologyBuses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...topologyEntity,
      ...values,
      powerLineBranchParent: topologyBuses.find(it => it.id.toString() === values.powerLineBranchParent.toString()),
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
          ...topologyEntity,
          powerLineBranchParent: topologyEntity?.powerLineBranchParent?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.topology.home.createOrEditLabel" data-cy="TopologyCreateUpdateHeading">
            Create or edit a Topology
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="topology-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Power Line Branch"
                id="topology-powerLineBranch"
                name="powerLineBranch"
                data-cy="powerLineBranch"
                type="text"
              />
              <ValidatedField label="P 1" id="topology-p1" name="p1" data-cy="p1" type="text" />
              <ValidatedField label="P 2" id="topology-p2" name="p2" data-cy="p2" type="text" />
              <ValidatedField
                id="topology-powerLineBranchParent"
                name="powerLineBranchParent"
                data-cy="powerLineBranchParent"
                label="Power Line Branch Parent"
                type="select"
              >
                <option value="" key="0" />
                {topologyBuses
                  ? topologyBuses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.powerLineBranch}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/topology" replace color="info">
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

export default TopologyUpdate;
