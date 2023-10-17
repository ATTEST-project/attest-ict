import React from 'react';
import './network-info.scss';
import { Button, Modal, ModalBody, ModalHeader, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import SLDEntire from 'app/modules/network/diagram/view/entire/network-sld-entire';
import CustomTooltip from 'app/shared/components/tooltip/custom-tooltip';
import ModalNetworkSLD from 'app/shared/components/modal-network-sld/modal-network-sld';
import { APP_DATE_FORMAT } from 'app/config/constants';

const NetworkInfo = props => {
  const { network } = props;

  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const toggleOpenModal = () => setOpenModal(!openModal);

  return (
    <div className="section-with-border">
      <span>{'Test Case selected'}</span>
      <Table responsive>
        <thead>
          <tr>
            <th>ID</th>
            <th>Network Name</th>
            <th>MPC Name</th>
            <th>Network Date</th>
            <th />
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{network.id}</td>
            <td>{network.name}</td>
            <td>{network.mpcName}</td>
            <td>{network.networkDate ? <TextFormat type="date" value={network.networkDate} format={APP_DATE_FORMAT} /> : null} </td>
            <td>
              <Button id="show-sld-button" size="sm" onClick={() => setOpenModal(true)}>
                <FontAwesomeIcon icon="project-diagram" />
              </Button>
              <CustomTooltip target="show-sld-button" tooltip="Show SLD" />
            </td>
          </tr>
        </tbody>
      </Table>
      {network && <ModalNetworkSLD network={network} openModal={openModal} toggleOpenModal={toggleOpenModal} />}
    </div>
  );
};

export default NetworkInfo;
