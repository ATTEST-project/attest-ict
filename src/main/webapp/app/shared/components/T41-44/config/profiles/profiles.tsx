import React from 'react';
import './profiles.scss';
import { AgGridReact } from 'ag-grid-react';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import { INetwork } from 'app/shared/model/network.model';
import { Button, Collapse, Modal, ModalBody, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import CustomTooltip from 'app/shared/components/tooltip/custom-tooltip';
import { useAppDispatch } from 'app/config/store';
import { getEntitiesByNetworkId } from 'app/entities/input-file/input-file.reducer';
import SLDEntire from 'app/modules/network/diagram/view/entire/network-sld-entire';
import GenLoadModal from 'app/modules/network/diagram/view/entire/gen-load-modal/gen-load-modal';
import { useFormContext } from 'react-hook-form';
import CellButton from 'app/shared/components/T41-44/config/profiles/cell-button/cell-button';
import axios from 'axios';
import { getSeason, getTypicalDay, getMode, getTimeInterval } from 'app/shared/util/profile-fields.utils';
import { SECTION } from 'app/shared/util/file-utils';

const ProfilesSection = props => {
  const { network, rowSelection } = props;
  const {
    register,
    setValue,
    formState: { errors },
    clearErrors,
  } = useFormContext();

  const dispatch = useAppDispatch();

  const gridRef = React.useRef<AgGridReact>(null);

  const [showProfiles, setShowProfiles] = React.useState<boolean>(false);

  const [rowData, setRowData] = React.useState(null);

  const [columnDefs, setColumnDefs] = React.useState([
    { headerName: 'Season', field: 'season', filter: true, valueFormatter: props => getSeason(props.value) },
    { headerName: 'Typical Day', field: 'typicalDay', filter: true, valueFormatter: props => getTypicalDay(props.value) },
    { headerName: 'Mode', field: 'mode', filter: true, valueFormatter: props => getMode(props.value) },
    { headerName: 'Time Interval', field: 'timeInterval', filter: true, valueFormatter: props => getTimeInterval(props.value) },
    { headerName: 'File Name', field: 'fileName', filter: true },
    { headerName: 'View Chart', field: '', cellRenderer: CellButton },
    { headerName: 'Download', field: '', cellRenderer: CellButton },
    { headerName: '', field: '', checkboxSelection: true },
  ]);

  const [profileSelected, setProfileSelected] = React.useState({
    profile: null,
    eventType: '',
  });
  const [openInFullscreen, setOpenInFullscreen] = React.useState(false);
  const toggleFullscreen = () => setOpenInFullscreen(!openInFullscreen);

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
    register('profiles');
    setValue('profiles', []);
    dispatch(getEntitiesByNetworkId(network.id))
      .unwrap()
      .then(res => {
        const files = res.data.filter(f => f.description === SECTION.LOAD || f.description === SECTION.ALL);
        setRowData([...files]);
      });
  }, []);

  const setProfileCallback = (profile, eventType) => {
    setProfileSelected({
      profile,
      eventType,
    });
  };

  const getSelectedRowData = () => {
    if (errors?.profiles) {
      clearErrors('profiles');
    }
    const selectedNodes = gridRef.current?.api.getSelectedNodes();
    /* if (selectedNodes.length === 0) {
      setError('profiles', { type: 'custom', message: 'Please select at least one profile' })
    } */
    const selectedData = selectedNodes.map(node => node.data);
    /* eslint-disable-next-line no-console */
    console.log('Selected Nodes: ', selectedData);
    setValue('profiles', selectedData);
  };

  /* React.useEffect(() => {
    if (rowData && rowData.length > 0) {
      register('profiles', { required: 'Please select at least one profile' });
    } else {
      register('profiles', { required: 'No profiles present. Upload load profiles data from the upload network page' });
    }
  }, [rowData]); */

  React.useEffect(() => {
    if (profileSelected.eventType === 'chart') {
      showChart(profileSelected.profile);
    } else if (profileSelected.eventType === 'download') {
      downloadProfile(profileSelected.profile);
    }
  }, [profileSelected]);

  const showChart = React.useCallback(profile => {
    /* eslint-disable-next-line no-console */
    console.log('Show chart of profile with id: ' + profile.id);
  }, []);

  const downloadProfile = React.useCallback(profile => {
    /* eslint-disable-next-line no-console */
    console.log('Download profile with id: ' + profile.id);
    const apiDownloadUrl = '/api/download-file/' + profile.id;
    axios
      .get(apiDownloadUrl, { responseType: 'blob' })
      .then(res => {
        const file = new Blob([res.data]);
        const url = window.URL.createObjectURL(file);
        const a = document.createElement('a');
        a.href = url;
        a.download = profile.fileName;
        a.click();
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.log(err);
      });
  }, []);

  return (
    <div className={['section-with-border', errors?.profiles ? 'not-selected-error' : null].join(' ')}>
      {errors?.profiles && <span>{errors.profiles.message}</span>}
      {rowData && rowData.length > 0 ? (
        <>
          <div
            style={{ display: 'flex', justifyContent: 'space-between', cursor: 'pointer' }}
            onClick={() => setShowProfiles(!showProfiles)}
          >
            <h6>{'Load Profiles'}</h6>
            <div style={{ marginRight: 10 }}>
              <FontAwesomeIcon icon="angle-down" style={showProfiles && { transform: 'rotate(180deg)' }} />
            </div>
          </div>
          <Collapse isOpen={showProfiles}>
            <div className="ag-theme-alpine-dark" style={{ width: '100%', height: '100%' }}>
              <AgGridReact
                ref={gridRef}
                rowData={rowData}
                columnDefs={columnDefs}
                defaultColDef={defaultColDef}
                domLayout={'autoHeight'}
                animateRows={true}
                onCellClicked={cellClickedListener}
                rowSelection={rowSelection}
                suppressRowClickSelection={true}
                onSelectionChanged={getSelectedRowData}
                pagination={true}
                paginationPageSize={10}
                onGridReady={event => event.api.sizeColumnsToFit()}
                onGridSizeChanged={showProfiles && (event => event.api.sizeColumnsToFit())}
                context={{ setProfileCallback }}
                overlayNoRowsTemplate={'<span>No profiles present. Upload load profiles data from the upload network page</span>'}
                // frameworkComponents={{btnCellRenderer: CellButton}}
              />
              {/* profileSelected && <GenLoadModal rect={profileSelected} openInFullscreen={openInFullscreen} toggleFullscreen={toggleFullscreen} networkEntity={network}/> */}
            </div>
          </Collapse>
        </>
      ) : (
        <div>{'No load profiles found!'}</div>
      )}
    </div>
  );
};

export default ProfilesSection;
