import { RouteComponentProps } from 'react-router-dom';
import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/task/task.reducer';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';

export const TaskLogFileDlDialog = (props: RouteComponentProps<{ id: string }>) => {
  const [loadModal, setLoadModal] = useState(false);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    setLoadModal(true);
  }, []);

  const taskEntity = useAppSelector(state => state.task.entity);
  const [operationCompleted, setOperationCompleted] = React.useState<boolean>(false);

  const handleClose = () => {
    props.history.push('/task' + props.location.search);
  };

  useEffect(() => {
    if (operationCompleted && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [operationCompleted]);

  const downloadLogFile = () => {
    const apiDownloadLogFileUrl = '/api/tool-log-files/taskId/' + taskEntity.id;
    axios
      .get(apiDownloadLogFileUrl, { responseType: 'arraybuffer' })
      .then(res => {
        const file = new Blob([res.data], { type: 'application/octet-stream' });
        const url = window.URL.createObjectURL(file);
        const a = document.createElement('a');
        a.href = url;
        a.download = taskEntity.dateTimeStart + '_' + taskEntity.tool.name + '_' + taskEntity.toolLogFileName;
        a.click();
        setOperationCompleted(true);
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.log(err);
      });
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="taskDownloadLogFileDialogHeading">
        Download Tool Log File
      </ModalHeader>
      <ModalBody id="attestApp.task.logFileDl.question">Are you sure you want to download log file of this Task?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-download-task" data-cy="entityConfirmDownloadButton" color="primary" onClick={downloadLogFile}>
          <FontAwesomeIcon icon="file-download" />
          &nbsp; Download
        </Button>
      </ModalFooter>
    </Modal>
  );
};
