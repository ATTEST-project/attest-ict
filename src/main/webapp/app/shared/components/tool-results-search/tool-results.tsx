import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Button, Modal, ModalBody, ModalHeader } from 'reactstrap';

import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine-dark.css';

import { IToolResult } from 'app/shared/model/tool-results.model';
import CustomTooltip from 'app/shared/components/tooltip/custom-tooltip';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import { APP_TIMESTAMP_FORMAT } from 'app/config/constants';
import { TextFormat } from 'react-jhipster';
import { reset } from 'app/shared/reducers/tool-results-search';

export const filterFileNameByKeywords = (fileList: IToolResult[], keywords: string[]): IToolResult[] => {
  return fileList.filter(file => keywords.some(keyword => file.fileName.toLowerCase().trim().includes(keyword.toLowerCase())));
};

const ToolResults = props => {
  const maxItemForPage = 15;
  const dispatch = useAppDispatch();
  const toolResultsAll = useAppSelector(state => state.toolResultsSearch.toolResults);
  const toolResults = props.filtersBy ? filterFileNameByKeywords(toolResultsAll, props.filtersBy) : toolResultsAll;
  const gridRef = React.useRef<AgGridReact>(null);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const toggleOpenModal = () => setOpenModal(!openModal);
  const [toolResultSelected, setToolResultSelected] = React.useState<IToolResult>(null);
  const [rowData, setRowData] = React.useState<IToolResult[]>();

  React.useEffect(() => {
    /* eslint-disable-next-line no-console */
    console.log('ToolResults - useEffect() -- toolResults: ', toolResults);
    if (!toolResults) {
      return;
    }
    setRowData(toolResults);
    return () => {
      // -- call  reset,  when unmount component (-> page exit)
      setRowData([]);
    };
  }, [toolResultsAll]);

  const [columnDefs, setColumnDefs] = React.useState([
    { headerName: 'Id', field: 'outputFileId', filter: true, maxWidth: 120 },
    { headerName: 'File Name', field: 'fileName', filter: true },
    { headerName: 'Simulation Description', field: 'simulationDescription', filter: true },
    { headerName: 'Task DateTime Start', field: 'dateTimeStart', filter: true, cellRenderer: 'customDateRenderer' },
    { headerName: 'Task DateTime End', field: 'dateTimeEnd', filter: true, cellRenderer: 'customDateRenderer' },
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
      resizable: true,
    }),
    []
  );

  const setToolResultCallback = (toolResult: IToolResult) => {
    setToolResultSelected(toolResult);
    setOpenModal(true);
  };

  const getSelectedRowData = () => {
    const selectedNodes = gridRef.current?.api.getSelectedNodes();
    const selectedData = selectedNodes.map(node => node.data);
    props.setRowsSelectedCallback(selectedData);
  };

  const handleResizeColum = event => {
    if (rowData && rowData.length > 0) {
      event.api.sizeColumnsToFit();
    }
  };

  // --- Convert UTC taskEnd/StartDate in 'DD/MM/YY HH:mm:ss' format.
  const CustomDateCellRenderer = props => {
    const { value } = props;
    const formattedDate = value ? <TextFormat type="date" value={value} format={APP_TIMESTAMP_FORMAT} /> : null;
    return formattedDate;
  };

  // --- Register your components using the `components` property
  const components = {
    customDateRenderer: CustomDateCellRenderer,
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
          rowSelection={props.selectionType || SELECTION_TYPE.MULTIPLE}
          suppressRowClickSelection={true}
          onSelectionChanged={getSelectedRowData}
          pagination={true}
          paginationPageSize={maxItemForPage}
          onGridReady={event => handleResizeColum(event)}
          onGridSizeChanged={event => handleResizeColum(event)}
          components={components} // Use the components property
          context={{ setToolResultCallback }}
        />
      </div>
    </div>
  );
};

export default ToolResults;
