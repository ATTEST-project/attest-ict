import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBranchExtension } from 'app/shared/model/branch-extension.model';
import { getEntities as getBranchExtensions } from 'app/entities/branch-extension/branch-extension.reducer';
import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './branch.reducer';
import { IBranch } from 'app/shared/model/branch.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { displayButton } from 'app/shared/reducers/back-button-display';

export const BranchUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const branchUrl = props.match.url.replace(/branch(\/.+)$/, 'branch');

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const branchExtensions = useAppSelector(state => state.branchExtension.entities);
  const networks = useAppSelector(state => state.network.entities);
  const branchEntity = useAppSelector(state => state.branch.entity);
  const loading = useAppSelector(state => state.branch.loading);
  const updating = useAppSelector(state => state.branch.updating);
  const updateSuccess = useAppSelector(state => state.branch.updateSuccess);
  const handleClose = () => {
    props.history.push(branchUrl + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBranchExtensions({}));
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
      ...branchEntity,
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
          ...branchEntity,
          network: branchEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.branch.home.createOrEditLabel" data-cy="BranchCreateUpdateHeading">
            Create or edit a Branch
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="branch-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Fbus" id="branch-fbus" name="fbus" data-cy="fbus" type="text" />
              <ValidatedField label="Tbus" id="branch-tbus" name="tbus" data-cy="tbus" type="text" />
              <ValidatedField label="R" id="branch-r" name="r" data-cy="r" type="text" />
              <ValidatedField label="X" id="branch-x" name="x" data-cy="x" type="text" />
              <ValidatedField label="B" id="branch-b" name="b" data-cy="b" type="text" />
              <ValidatedField label="Ratea" id="branch-ratea" name="ratea" data-cy="ratea" type="text" />
              <ValidatedField label="Rateb" id="branch-rateb" name="rateb" data-cy="rateb" type="text" />
              <ValidatedField label="Ratec" id="branch-ratec" name="ratec" data-cy="ratec" type="text" />
              <ValidatedField label="Tap Ratio" id="branch-tapRatio" name="tapRatio" data-cy="tapRatio" type="text" />
              <ValidatedField label="Angle" id="branch-angle" name="angle" data-cy="angle" type="text" />
              <ValidatedField label="Status" id="branch-status" name="status" data-cy="status" type="text" />
              <ValidatedField label="Angmin" id="branch-angmin" name="angmin" data-cy="angmin" type="text" />
              <ValidatedField label="Angmax" id="branch-angmax" name="angmax" data-cy="angmax" type="text" />
              <ValidatedField id="branch-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to={branchUrl} replace color="info">
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

export default BranchUpdate;
