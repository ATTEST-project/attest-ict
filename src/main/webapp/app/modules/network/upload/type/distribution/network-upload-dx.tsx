import React from 'react';
import './network-upload-dx.scss';
import UploadFileRowDT from 'app/modules/network/upload/file-row/upload-file-row-dt';
import LabelsRow from 'app/modules/network/upload/file-row/labels-row';
import FilesUploaded from 'app/modules/network/upload/files-uploaded/files-uploaded';
import { useAppSelector } from 'app/config/store';
import { SECTION } from 'app/shared/util/file-utils';

const NetworkUploadDX = props => {
  const { network } = props;

  const [genNRows, setGenNRows] = React.useState(1);
  const callbackGenNRows = (value: number) => setGenNRows(prevState => prevState + value);

  const [loadNRows, setLoadNRows] = React.useState(1);
  const callbackLoadNRows = (value: number) => setLoadNRows(prevState => prevState + value);

  const [flexNRows, setFlexNRows] = React.useState(1);
  const callbackFlexNRows = (value: number) => setFlexNRows(prevState => prevState + value);

  const inputFileEntities = useAppSelector(state => state.inputFile.entities);

  return (
    <>
      <div className="upload-file-row">
        <div className="title-row">
          <span style={{ fontSize: 20 }}>{'Generator Profile'}</span>
          {inputFileEntities && <FilesUploaded networkId={network.id} section={SECTION.GENERATOR} files={inputFileEntities} />}
        </div>
        {/* <LabelsRow section="gen" /> */}
        {[...Array(genNRows).keys()].map((e, i) => (
          <div key={i} style={{ marginBottom: 20 }}>
            <UploadFileRowDT
              index={i}
              acceptType=".csv,.xlsx"
              section={SECTION.GENERATOR}
              nRows={genNRows}
              callbackNRows={callbackGenNRows}
              networkId={network.id}
            />
          </div>
        ))}
      </div>
      <div className="upload-file-row">
        <div className="title-row">
          <span style={{ fontSize: 20 }}>{'Load Profile'}</span>
          {inputFileEntities && <FilesUploaded networkId={network.id} section={SECTION.LOAD} files={inputFileEntities} />}
        </div>
        {/* <LabelsRow section="load" /> */}
        {[...Array(loadNRows).keys()].map((e, i) => (
          <div key={i} style={{ marginBottom: 20 }}>
            <UploadFileRowDT
              index={i}
              acceptType=".csv,.xlsx"
              section={SECTION.LOAD}
              nRows={loadNRows}
              callbackNRows={callbackLoadNRows}
              networkId={network.id}
            />
          </div>
        ))}
      </div>
      <div className="upload-file-row">
        <div className="title-row">
          <span style={{ fontSize: 20 }}>{'Flex Profile'}</span>
          {inputFileEntities && <FilesUploaded networkId={network.id} section={SECTION.FLEXIBILITY} files={inputFileEntities} />}
        </div>
        {/* <LabelsRow section="flex" /> */}
        {[...Array(flexNRows).keys()].map((e, i) => (
          <div key={i} style={{ marginBottom: 20 }}>
            <UploadFileRowDT
              index={i}
              acceptType=".csv,.xlsx"
              section={SECTION.FLEXIBILITY}
              nRows={flexNRows}
              callbackNRows={callbackFlexNRows}
              networkId={network.id}
            />
          </div>
        ))}
      </div>
    </>
  );
};

export default NetworkUploadDX;
