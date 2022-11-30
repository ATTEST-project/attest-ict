import React from 'react';
import { AgGridReact } from 'ag-grid-react';

import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine-dark.css';
import { Button, Modal, ModalBody, ModalHeader } from 'reactstrap';
import { INetwork } from 'app/shared/model/network.model';
import SLDEntire from 'app/modules/network/diagram/view/entire/network-sld-entire';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import CustomTooltip from 'app/shared/components/tooltip/custom-tooltip';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import ModalNetworkSLD from 'app/shared/components/modal-network-sld/modal-network-sld';

const CellButton = (props: any) => {
  const network = props.data;

  const buttonClicked = () => {
    props.context.setNetworkCallback(network);
  };

  return (
    <Button id={'sld-button-' + props.node.rowIndex} disabled={!props.data.mpcName} onClick={() => buttonClicked()}>
      <FontAwesomeIcon icon="project-diagram" />
      <CustomTooltip target={'sld-button-' + props.node.rowIndex} tooltip="Show SLD" />
    </Button>
  );
};

const NetworkSearchResults = props => {
  const gridRef = React.useRef<AgGridReact>(null);

  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const toggleOpenModal = () => setOpenModal(!openModal);
  const [networkSelected, setNetworkSelected] = React.useState<INetwork>(null);

  const [rowData, setRowData] = React.useState<INetwork[]>();

  const [columnDefs, setColumnDefs] = React.useState([
    { headerName: 'Network Date', field: 'networkDate', filter: true },
    { headerName: 'Name', field: 'name', filter: true },
    { headerName: 'MPC Name', field: 'mpcName', filter: true },
    { headerName: '', field: 'showSLD', cellRendererSelector: params => (params.data.mpcName ? { component: CellButton } : null) },
    {
      headerName: '',
      field: 'selected',
      headerCheckboxSelection: props.selectionType === SELECTION_TYPE.SINGLE ? null : true,
      checkboxSelection: true,
    },
  ]);

  const defaultColDef = React.useMemo(
    () => ({
      sortable: true,
    }),
    []
  );

  const cellClickedListener = React.useCallback(event => {
    /* eslint-disable-next-line no-console */
    console.log('Cell clicked: ', event);
  }, []);

  React.useEffect(() => {
    const filteredNetworks = props.networks.filter(n => n.mpcName);
    setRowData([...filteredNetworks]);
  }, []);

  const setNetworkCallback = (network: INetwork) => {
    setNetworkSelected(network);
    setOpenModal(true);
  };

  const getSelectedRowData = () => {
    const selectedNodes = gridRef.current?.api.getSelectedNodes();
    const selectedData = selectedNodes.map(node => node.data);
    /* eslint-disable-next-line no-console */
    console.log('Selected Nodes: ', selectedData);
    props.setRowsSelectedCallback(selectedData);
  };

  return (
    <div>
      <div className="ag-theme-alpine-dark" style={{ width: '100%', height: '100%' }}>
        <AgGridReact
          ref={gridRef}
          rowData={rowData}
          columnDefs={columnDefs}
          defaultColDef={defaultColDef}
          domLayout={'autoHeight'}
          animateRows={true}
          onCellClicked={cellClickedListener}
          rowSelection={props.selectionType || SELECTION_TYPE.MULTIPLE}
          suppressRowClickSelection={true}
          onSelectionChanged={getSelectedRowData}
          pagination={true}
          paginationPageSize={10}
          onGridReady={event => event.api.sizeColumnsToFit()}
          onGridSizeChanged={event => event.api.sizeColumnsToFit()}
          context={{ setNetworkCallback }}
          // frameworkComponents={{btnCellRenderer: CellButton}}
        />
      </div>
      {networkSelected && <ModalNetworkSLD network={networkSelected} openModal={openModal} toggleOpenModal={toggleOpenModal} />}
    </div>
  );
};

export default NetworkSearchResults;
