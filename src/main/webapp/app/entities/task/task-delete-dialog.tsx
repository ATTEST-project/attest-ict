import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity, deleteEntity, getEntities } from './task.reducer';

import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { hasAuthorityForUpdateTask, shouldFilterTask } from 'app/shared/util/authorizationUtils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';

export const TaskDeleteDialog = (props: RouteComponentProps<{ id: string }>) => {
  const userEntity = useAppSelector(state => state.authentication.account);
  const shouldFilter = shouldFilterTask(userEntity.authorities);
  const [paginationState, setPaginationState] = useState({ activePage: 1, itemsPerPage: ITEMS_PER_PAGE, sort: 'id', order: 'desc' });
  const [loadModal, setLoadModal] = useState(false);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    setLoadModal(true);
  }, []);

  const taskEntity = useAppSelector(state => state.task.entity);
  const updateSuccess = useAppSelector(state => state.task.updateSuccess);

  const handleClose = () => {
    const params = {
      page: paginationState.activePage - 1,
      size: paginationState.itemsPerPage,
      sort: `${paginationState.sort},${paginationState.order}`,
    };
    const requestParam = shouldFilter ? { ...params, userId: userEntity.id } : params;
    dispatch(getEntities(requestParam));
    props.history.push('/task' + props.location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(taskEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="taskDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="attestApp.task.delete.question">Are you sure you want to delete the Task {taskEntity.id}? </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-task" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default TaskDeleteDialog;
