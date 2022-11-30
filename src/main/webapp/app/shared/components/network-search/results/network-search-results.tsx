import React from 'react';
import { Button, Input, Modal, ModalBody, ModalHeader, Spinner, Table } from 'reactstrap';
import { NetworkEntireSLDProps } from 'app/shared/model/network-sld.model';
import { useAppDispatch } from 'app/config/store';
import { generateEntireSLDById } from 'app/entities/network/network-sld-entire.reducer';
import SVGHandler from 'app/modules/network/diagram/view/entire/svg-handler/svgdotjs/svg-handler';

interface NetworkSVGProps {
  id: string;
  mpcName: string;
  svgData: NetworkEntireSLDProps;
}

const NetworkSearchResults = props => {
  const { networks } = props;

  const dispatch = useAppDispatch();

  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [loading, setLoading] = React.useState<boolean>(false);
  const [networkSvg, setNetworkSvg] = React.useState<NetworkSVGProps>(null);

  const handleButtonClick = network => {
    setLoading(true);
    setOpenModal(true);
    dispatch(generateEntireSLDById(network.id))
      .unwrap()
      .then(res => {
        setNetworkSvg({
          id: network.id,
          mpcName: network.mpcName,
          svgData: res.data,
        });
        setLoading(false);
      })
      .catch(err => {
        setNetworkSvg(null);
        setOpenModal(false);
        setLoading(false);
      });
  };

  return (
    <>
      <Table>
        <thead>
          <tr>
            <th>Date</th>
            <th>Name</th>
            <th>MPC Name</th>
            <th />
            <th>Select</th>
          </tr>
        </thead>
        <tbody>
          {networks.map((network, index) => (
            <tr key={index}>
              <td>{network.networkDate}</td>
              <td>{network.name}</td>
              <td>{network.mpcName}</td>
              <td>
                <Button color="info" onClick={() => handleButtonClick(network)}>
                  Show SLD
                </Button>
              </td>
              <td>
                <Input type="checkbox" />
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Modal isOpen={openModal} fullscreen>
        <ModalHeader toggle={() => setOpenModal(false)}>{networkSvg?.mpcName || ''}</ModalHeader>
        <ModalBody style={{ background: 'white', overflow: 'hidden' }}>
          {loading ? (
            <div style={{ width: '100%', height: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
              <Spinner color="info" />
            </div>
          ) : (
            networkSvg?.svgData && <SVGHandler network={networkSvg.svgData} />
          )}
        </ModalBody>
      </Modal>
    </>
  );
};

export default NetworkSearchResults;
