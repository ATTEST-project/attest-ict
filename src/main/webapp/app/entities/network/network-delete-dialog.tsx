import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Spinner } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './network.reducer';

export const NetworkDeleteDialog = (props: RouteComponentProps<{ id: string }>) => {
  const [loadModal, setLoadModal] = useState(false);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    setLoadModal(true);
  }, []);

  const networkEntity = useAppSelector(state => state.network.entity);
  const updateSuccess = useAppSelector(state => state.network.updateSuccess);

  const [loading, setLoading] = React.useState(false);

  const handleClose = () => {
    props.history.push('/network' + props.location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    setLoading(true);
    dispatch(deleteEntity(networkEntity.id))
      .unwrap()
      .then(res => {
        setLoading(false);
        const networkSaved = sessionStorage.getItem('network');
        if (networkSaved && JSON.parse(networkSaved).id === networkEntity.id) {
          sessionStorage.removeItem('network');
        }
        handleClose();
      })
      .catch(err => setLoading(false));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="networkDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="attestApp.network.delete.question">Are you sure you want to delete this Network?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        {!loading ? (
          <Button id="jhi-confirm-delete-network" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
            <FontAwesomeIcon icon="trash" />
            {' Delete'}
          </Button>
        ) : (
          <Button color="danger" disabled>
            <Spinner color="light" size="sm" />
          </Button>
        )}
      </ModalFooter>
    </Modal>
  );
};

export default NetworkDeleteDialog;
