import LabelsRow from 'app/modules/network/upload/file-row/labels-row';
import UploadFileRowDT from 'app/modules/network/upload/file-row/upload-file-row-dt';
import React from 'react';
import { useAppSelector } from 'app/config/store';
import FilesUploaded from 'app/modules/network/upload/files-uploaded/files-uploaded';

const NetworkUploadTX = props => {
  const { network } = props;

  const inputFileEntities = useAppSelector(state => state.inputFile.entities);

  return (
    <>
      <div className="upload-file-row">
        <div className="title-row">
          <span style={{ fontSize: 20 }}>{'h24 Profiles'}</span>
          {inputFileEntities && <FilesUploaded networkId={network.id} files={inputFileEntities} isTx />}
        </div>
        {/* <LabelsRow section="gen" /> */}
        <div style={{ marginBottom: 20 }}>
          <UploadFileRowDT acceptType=".xlsx" networkId={network.id} isTX />
        </div>
      </div>
    </>
  );
};

export default NetworkUploadTX;
