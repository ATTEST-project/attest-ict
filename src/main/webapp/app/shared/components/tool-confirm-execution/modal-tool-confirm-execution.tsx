import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

import Divider from 'app/shared/components/divider/divider';
import {
  TOOL_EXECUTION_CONFIRM_HEADER,
  TOOL_EXECUTION_CONFIRM_QUESTION,
  RUN_BUTTON_LABEL,
  CANCEL_BUTTON_LABEL,
} from 'app/shared/util/modal-labels';

import { getToolNum } from 'app/shared/util/tool-info-utils';
import { isUserAdmin } from 'app/shared/util/authorizationUtils';

const ModalConfirmToolExecution = (props: any) => {
  const { toolDescription, form, openModal, checkAndRun, setOpenModal } = props;
  const currentUser = useAppSelector(state => state.authentication.account);
  const authorities = currentUser.authorities;

  return (
    <Modal isOpen={openModal}>
      <ModalHeader toggle={() => setOpenModal(false)} data-cy="tool.config.execution.confirm.heading">
        {toolDescription}
      </ModalHeader>
      <ModalBody id="tool.config.run.question">
        {TOOL_EXECUTION_CONFIRM_QUESTION}
        {isUserAdmin(authorities) && (
          <>
            <Divider />
            {'Check the configuration...'}
            {form.files.size > 0 ? (
              <pre>{JSON.stringify({ ...form, files: form.files?.map((file: File) => file.name) }, null, 2)}</pre>
            ) : (
              <pre>{JSON.stringify({ ...form }, null, 2)}</pre>
            )}
          </>
        )}
      </ModalBody>
      <ModalFooter>
        <Button onClick={() => setOpenModal(false)}>{CANCEL_BUTTON_LABEL}</Button>
        <Button color="primary" onClick={checkAndRun}>
          {RUN_BUTTON_LABEL}
        </Button>{' '}
      </ModalFooter>
    </Modal>
  );
};

export default ModalConfirmToolExecution;
