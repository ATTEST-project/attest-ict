import React from 'react';
import './modal-network-sld.scss';
import { Modal, ModalBody, ModalHeader } from 'reactstrap';
import SLDEntire from 'app/modules/network/diagram/view/entire/network-sld-entire';

const ModalNetworkSLD = props => {
  const { network, openModal, toggleOpenModal } = props;

  const [windowOffset, setWindowOffset] = React.useState(0);

  const modalOpenedEvent = () => {
    setWindowOffset(window.scrollY);
    document.body.classList.add('modal-open-custom');
  };

  const modalClosedEvent = () => {
    document.body.classList.remove('modal-open-custom');
    window.scrollTo(0, windowOffset);
  };

  return (
    <Modal isOpen={openModal} fullscreen returnFocusAfterClose={false} onOpened={modalOpenedEvent} onClosed={modalClosedEvent}>
      <ModalHeader toggle={toggleOpenModal}>{network?.mpcName || ''}</ModalHeader>
      <ModalBody className="modal-body-svg">
        {network && (
          <div id="svg-container-id">
            <SLDEntire network={network} containerId={'svg-container-id'} />
          </div>
        )}
      </ModalBody>
    </Modal>
  );
};

export default ModalNetworkSLD;
