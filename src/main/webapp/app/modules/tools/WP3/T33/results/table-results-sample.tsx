import React from 'react';
import { AgGridReact } from 'ag-grid-react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine-dark.css';
import { Button, Modal, ModalBody, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import tableData from 'app/modules/tools/WP3/T33/sample-data/response_table.json';

const Table = (props: any) => {
  const [rowData, setRowData] = React.useState(tableData?.rowData);
  const [columnDefs, setColumnDefs] = React.useState(tableData?.columnDefs);
  const gridRef = React.useRef<AgGridReact>(null);

  return (
    <div>
      <div className="ag-theme-alpine-dark" style={{ width: '100%', height: '100%' }}>
        <div className="ag-theme-alpine" style={{ height: 400, width: '100%' }}>
          <AgGridReact rowData={rowData} columnDefs={columnDefs}></AgGridReact>
        </div>
      </div>
    </div>
  );
};

export default Table;
