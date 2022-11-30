import React from 'react';
import './spreadsheet.scss';
import Spreadsheet from 'x-data-spreadsheet';
import { stox, xtos } from 'app/shared/components/spreadsheet/utils/xlsx-utils';
import * as XLSX from 'xlsx';
import { Button, Input, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import { tractabilitySampleData } from './sample-data/tractability-sample-data';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { toast } from 'react-toastify';

interface CustomDivRef extends HTMLDivElement {
  [key: string]: any;
}

const SpreadSheet = (props: any) => {
  const { gridData, file } = props;

  const divRef = React.useRef<CustomDivRef>();

  const options = React.useMemo(
    () => ({
      view: {
        height: () => document.documentElement.clientHeight - 200,
        width: () => document.documentElement.clientWidth - 50,
      },
    }),
    []
  );

  const data = gridData || [...tractabilitySampleData];

  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const toggleModal = () => setOpenModal(!openModal);

  const [fileName, setFileName] = React.useState<string>('export');
  const [extension, setExtension] = React.useState<string>('.xlsx');

  const [loadingFile, setLoadingFile] = React.useState<boolean>(false);

  React.useEffect(() => {
    if (divRef.current && !divRef.current.grid) {
      divRef.current.grid = new Spreadsheet(divRef.current, options).loadData(data);
      // fix for scroll only inside
      const overlayerEl = divRef.current.grid?.sheet?.overlayerEl?.el;
      overlayerEl.addEventListener('mousewheel', event => {
        event.preventDefault();
        const scrollTo = event.deltaY;
        overlayerEl.scrollTo(0, scrollTo);
      });
    }
  }, [divRef]);

  React.useEffect(() => {
    loadFileAsBinary(file);
  }, [file]);

  /* React.useEffect(() => {
    document.body.style.overflow = 'hidden';
    document.body.style.position = 'fixed';
    return () => {
      document.body.style.overflow = '';
      document.body.style.position = '';
    };
  }, [openModal]) */

  const showLoadError = () => {
    toast.error('Error loading file!', {
      position: 'top-right',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
    const xSpr = divRef.current?.grid;
    xSpr.loadData({});
  };

  const resizeBottomBar = data => {
    const bottomBar = divRef.current?.grid?.bottombar?.el?.el;
    if (data.length > 7) {
      bottomBar.classList.add('custom-bottombar');
    } else {
      bottomBar.classList.remove('custom-bottombar');
    }
  };

  const processWB = wb => {
    const xSpr = divRef.current?.grid;
    // convert workbook to x-spreadsheet
    const data = stox(wb);
    // load data into x-spreadsheet
    xSpr.loadData(data);
    resizeBottomBar(data);
  };

  const loadFileAsArrayBuffer = event => {
    const file = event.target.files[0];
    if (!file) {
      return;
    }
    setLoadingFile(true);
    const reader = new FileReader();
    reader.onload = function (e) {
      const data: any = e.target.result;
      try {
        const workbook = XLSX.read(new Uint8Array(data), { type: 'array' });
        processWB(workbook);
      } catch (err) {
        showLoadError();
      } finally {
        setLoadingFile(false);
      }
    };
    reader.onerror = function (err) {
      /* eslint-disable-next-line no-console */
      console.log(err);
      setLoadingFile(false);
    };
    reader.readAsArrayBuffer(file);
  };

  const loadFileAsBinary = event => {
    const fileChosen = event?.target?.files[0] || event;
    if (!fileChosen) {
      return;
    }
    setLoadingFile(true);

    const reader = new FileReader();
    reader.onload = function (e) {
      const data = e.target.result;
      try {
        const workbook = XLSX.read(data, { type: 'binary' });
        /* workbook.SheetNames.forEach(function (sheetName) {
          const sheet = workbook.Sheets[sheetName];
          const XLRowObject = XLSX.utils.sheet_to_json(sheet);
          /!* eslint-disable-next-line no-console *!/
          console.log(sheetName, XLRowObject);
        }); */
        processWB(workbook);
      } catch (err) {
        showLoadError();
      } finally {
        setLoadingFile(false);
      }
    };
    reader.onerror = function (err) {
      /* eslint-disable-next-line no-console */
      console.log(err);
      showLoadError();
      setLoadingFile(false);
    };
    reader.readAsBinaryString(fileChosen);
  };

  const downloadFile = () => {
    const xSpr = divRef.current?.grid;
    /* eslint-disable-next-line no-console */
    console.log('Grid data: ', xSpr.getData());
    // sessionStorage.setItem('excelGridValues', JSON.stringify(xSpr.getData()));
    const new_wb = xtos(xSpr.getData());
    XLSX.writeFile(new_wb, fileName + extension);
    toggleModal();
  };

  return (
    <>
      <Input type="file" onChange={loadFileAsBinary} style={{ width: 350 }} />
      <div style={{ marginBottom: 5 }} />
      <div
        id="x-spreadsheet-demo"
        ref={divRef}
        // style={{ width: "100%", height: 300 }}
      >
        {loadingFile && <LoadingOverlay ref={divRef} />}
      </div>
      <div style={{ marginTop: 5, marginBottom: 10 }} />
      <Button onClick={toggleModal} color="primary" style={{ float: 'right' }}>
        <FontAwesomeIcon icon="file-download" />
        {' Export Data'}
      </Button>
      <Modal isOpen={openModal} toggle={toggleModal} centered>
        <ModalHeader toggle={toggleModal}>{'Download XLSX/ODS File'}</ModalHeader>
        <ModalBody>
          <div style={{ marginBottom: 10 }}>
            {'Are you sure you want to download an XLSX/ODS file? Choose the name and the format of the file'}
          </div>
          <div style={{ display: 'flex' }}>
            <Input type="text" placeholder="File name..." onChange={event => setFileName(event.target.value)} />
            <Input type="select" style={{ flex: '1 1 50%' }} onChange={event => setExtension(event.target.value)}>
              <option value="" hidden>
                Format...
              </option>
              <option value=".xlsx">XLSX</option>
              <option value=".ods">ODS</option>
            </Input>
          </div>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={toggleModal}>
            <FontAwesomeIcon icon="ban" />
            {' Cancel'}
          </Button>
          <Button color="primary" onClick={downloadFile}>
            <FontAwesomeIcon icon="file-download" />
            {' Download'}
          </Button>
        </ModalFooter>
      </Modal>
    </>
  );
};

export default SpreadSheet;
