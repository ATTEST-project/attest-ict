import React from 'react';
import './network-info.scss';
import { Button, Modal, ModalBody, ModalHeader, Table } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import SLDEntire from 'app/modules/network/diagram/view/entire/network-sld-entire';
import CustomTooltip from 'app/shared/components/tooltip/custom-tooltip';
import ModalNetworkSLD from 'app/shared/components/modal-network-sld/modal-network-sld';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { INetwork } from 'app/shared/model/network.model';
import TextTruncate from 'app/shared/components/text/text-truncate';
import SectionHeader from 'app/shared/components/section-header/section-header';

interface NetworkInfoInterface {
  network: INetwork;
  hideSld?: boolean;
}

const NetworkInfo = (props: NetworkInfoInterface) => {
  const network = props.network;
  const showSld = props.hideSld ? false : true;
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const toggleOpenModal = () => setOpenModal(!openModal);

  return (
    <div className="section-with-border">
      <SectionHeader title="Network selected" />
      <Table responsive>
        <thead>
          <tr>
            <th>ID</th>
            <th>Network </th>
            <th>Country</th>
            <th>Type</th>
            <th>Network Date</th>
            <th />
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{network.id}</td>
            <td>
              {network.mpcName ? (
                <TextTruncate maxWidth={'550px'} text={network.name + ' - ' + network.mpcName} />
              ) : (
                <span>{network.name}</span>
              )}
            </td>
            <td>{network.country}</td>
            <td>{network.type}</td>
            <td>{network.networkDate ? <TextFormat type="date" value={network.networkDate} format={APP_DATE_FORMAT} /> : null} </td>
            {showSld && (
              <td>
                <Button id="show-sld-button" size="sm" onClick={() => setOpenModal(true)}>
                  <FontAwesomeIcon icon="project-diagram" />
                </Button>
                <CustomTooltip target="show-sld-button" tooltip="Show SLD" />
              </td>
            )}
          </tr>
        </tbody>
      </Table>
      {network && showSld && <ModalNetworkSLD network={network} openModal={openModal} toggleOpenModal={toggleOpenModal} />}
    </div>
  );
};

export default NetworkInfo;
