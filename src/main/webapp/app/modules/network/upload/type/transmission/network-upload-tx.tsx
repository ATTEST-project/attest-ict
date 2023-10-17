import LabelsRow from 'app/modules/network/upload/file-row/labels-row';
import UploadFileRowDT from 'app/modules/network/upload/file-row/upload-file-row-dt';
import React from 'react';
import { useAppSelector } from 'app/config/store';
import FilesUploaded from 'app/modules/network/upload/files-uploaded/files-uploaded';

const NetworkUploadTX = props => {
  const { network } = props;

  const inputFileEntities = useAppSelector(state => state.inputFile.entities);

  const [txNRows, setTxNRows] = React.useState(1);
  const callbackTxNRows = (value: number) => setTxNRows(prevState => prevState + value);

  return (
    <>
      <div className="upload-file-row">
        <div className="title-row">
          <span style={{ fontSize: 20 }}>{'h24 Profiles'}</span>
          {inputFileEntities && <FilesUploaded networkId={network.id} files={inputFileEntities} isTx />}
        </div>
        {/* <LabelsRow section="gen" /> */}

        {[...Array(txNRows).keys()].map((e, i) => (
          <div key={i} style={{ marginBottom: 20 }}>
            <UploadFileRowDT acceptType=".xlsx" nRows={txNRows} callbackNRows={callbackTxNRows} networkId={network.id} isTX />
          </div>
        ))}
      </div>
    </>
  );
};

export default NetworkUploadTX;
