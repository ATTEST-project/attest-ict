import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Button, Modal, ModalBody, ModalHeader } from 'reactstrap';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine-dark.css';
import CustomTooltip from 'app/shared/components/tooltip/custom-tooltip';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import { APP_TIMESTAMP_FORMAT } from 'app/config/constants';
import { TextFormat } from 'react-jhipster';
import { ICimRepoNetwork } from 'app/shared/model/cim-repo-network.model';

const NetworkFromCimRepoGrid = props => {
  const maxItemForPage = 15;
  const dispatch = useAppDispatch();
  const cimNetworks = useAppSelector(state => state.networkImportFromCimRepo.cimNetworks); // array with networkId, networkName retrieve from the cim repo
  const gridRef = React.useRef<AgGridReact>(null);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const toggleOpenModal = () => setOpenModal(!openModal);
  const [networkSelected, setNetworkSelected] = React.useState<ICimRepoNetwork>(null);
  const [rowData, setRowData] = React.useState<ICimRepoNetwork[]>();

  React.useEffect(() => {
    setRowData(cimNetworks);
  }, [cimNetworks]);

  const [columnDefs, setColumnDefs] = React.useState([
    { headerName: 'Id', field: 'networkId', width: 30, sortable: true },
    { headerName: 'Network Name', field: 'networkName', filter: true, sortable: true, maxWith: 60 },
    {
      headerName: '',
      field: 'selected',
      sortable: false,
      filter: false,
      width: 50,
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

  const setResultCallback = (networkSelected: ICimRepoNetwork) => {
    setNetworkSelected(networkSelected);
    setOpenModal(true);
  };

  const getSelectedRowData = () => {
    const selectedNodes = gridRef.current?.api.getSelectedNodes();
    const selectedData = selectedNodes.map(node => node.data);
    /* eslint-disable-next-line no-console */
    console.log('getSelectedRowData() rows selected: ', selectedData);
    props.setRowsSelectedCallback(selectedData);
  };

  const handleResizeColum = event => {
    if (rowData && rowData.length > 0) {
      event.api.sizeColumnsToFit();
    }
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
          rowSelection={props.selectionType || SELECTION_TYPE.SINGLE}
          suppressRowClickSelection={true}
          onSelectionChanged={getSelectedRowData}
          pagination={true}
          paginationPageSize={maxItemForPage}
          onGridReady={event => handleResizeColum(event)}
          onGridSizeChanged={event => handleResizeColum(event)}
          context={{ setResultCallback }}
          overlayNoRowsTemplate={'<span> Networks not found! </span>'}
        />
      </div>
    </div>
  );
};

export default NetworkFromCimRepoGrid;
