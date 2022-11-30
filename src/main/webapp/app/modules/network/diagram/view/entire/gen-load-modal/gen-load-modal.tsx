import React from 'react';
import { Modal, ModalBody, ModalHeader } from 'reactstrap';
import ProfileChart from 'app/modules/network/diagram/view/entire/chart/profile-chart';
import { INetwork } from 'app/shared/model/network.model';

interface GenLoadModalProps {
  rect: {
    id: string;
    component: string;
  };
  openInFullscreen: boolean;
  toggleFullscreen: () => void;
  networkEntity: INetwork;
}

const GenLoadModal = (props: GenLoadModalProps) => {
  const { rect, openInFullscreen, toggleFullscreen, networkEntity } = props;

  return (
    <Modal isOpen={openInFullscreen} toggle={toggleFullscreen} fullscreen zIndex={9999}>
      <ModalHeader toggle={toggleFullscreen}>
        <span>{rect.component}</span>
      </ModalHeader>
      <ModalBody>
        <ProfileChart isFullscreen={true} component={rect.component} network={networkEntity} />
      </ModalBody>
    </Modal>
  );
};

export default GenLoadModal;
