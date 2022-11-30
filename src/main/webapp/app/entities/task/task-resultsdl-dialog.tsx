import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/task/task.reducer';
import axios from 'axios';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getFileTypeAndExtension } from 'app/shared/util/file-utils';

const TaskResultsDlDialog = (props: any) => {
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

  const downloadResultsFile = () => {
    const apiDownloadResultsFileUrl = '/api/tasks/tool-results/' + taskEntity.id;
    axios
      .get(apiDownloadResultsFileUrl, { responseType: 'blob' })
      .then(res => {
        const { mimeType, ext } = getFileTypeAndExtension(res.headers['content-type']);
        const file = new Blob([res.data], { type: mimeType });
        const url = window.URL.createObjectURL(file);
        const a = document.createElement('a');
        a.href = url;
        a.download = taskEntity.tool.name + '_' + taskEntity.simulationUuid + ext;
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
      <ModalHeader toggle={handleClose} data-cy="taskDownloadResultsFileDialogHeading">
        Download Tool Results File
      </ModalHeader>
      <ModalBody id="attestApp.task.resultsFileDl.question">Are you sure you want to download results file of this Task?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-download-task" data-cy="entityConfirmDownloadButton" color="primary" onClick={downloadResultsFile}>
          <FontAwesomeIcon icon="file-download" />
          &nbsp; Download
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default TaskResultsDlDialog;
