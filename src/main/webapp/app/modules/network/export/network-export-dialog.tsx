import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/network/network.reducer';
import { Button, Input, Modal, ModalBody, ModalFooter, ModalHeader, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Divider from 'app/shared/components/divider/divider';
import { exportToMatpower, exportToODS } from 'app/entities/network/network-export.reducer';

export const NetworkExportDialog = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [loadModal, setLoadModal] = useState(false);
  const [fileExt, setFileExt] = useState<string>('');

  const loading = useAppSelector(state => state.networkExport.loading);
  const operationCompleted = useAppSelector(state => state.networkExport.updateSuccess);

  React.useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    setLoadModal(true);
  }, []);

  const networkEntity = useAppSelector(state => state.network.entity);

  useEffect(() => {
    if (operationCompleted && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [operationCompleted]);

  const handleClose = () => {
    props.history.push('/network');
  };

  const exportData = () => {
    if (fileExt === '.m') {
      dispatch(exportToMatpower(networkEntity));
    } else if (fileExt === '.ods') {
      dispatch(exportToODS(networkEntity));
    } else {
      return;
    }
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="networkExportDialogHeading">
        Export
      </ModalHeader>
      <ModalBody id="attestApp.network.export.text">
        {"Are you sure you want to export data of network '" + networkEntity.name + "'?"}
        <Divider />
        <div>
          {' Choose the format of the file '}
          <Input type="select" onChange={event => setFileExt(event.target.value)}>
            <option value="" hidden>
              Format...
            </option>
            <option value=".m">Matpower</option>
            <option value=".ods">ODS</option>
          </Input>
        </div>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button
          id="jhi-confirm-export-network"
          disabled={!fileExt || loading}
          data-cy="entityConfirmExportButton"
          color="primary"
          onClick={exportData}
        >
          {loading ? (
            <Spinner color="light" size="sm" />
          ) : (
            <>
              <FontAwesomeIcon icon="file-download" />
              {' Export'}
            </>
          )}
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default NetworkExportDialog;
