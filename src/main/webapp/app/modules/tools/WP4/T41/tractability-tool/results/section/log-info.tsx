import React from 'react';
import { Table } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';

interface LogInfoInterface {
  logInfos: any;
}

const LogInfo = (props: LogInfoInterface) => {
  const { logInfos } = props;

  return (
    <>
      <div className="section-with-border">
        <Table responsive className="table-responsive">
          <thead>
            <tr>
              <th>Case Name</th>
              <th>Start Time</th>
              <th>N Violations</th>
              <th>OPT Started</th>
              <th>OPT Found</th>
            </tr>
          </thead>
          <tbody>
            {logInfos?.map((logInfo, index) => (
              <tr key={index}>
                <td> {logInfo.caseName} </td>
                <td> {logInfo.startTime} </td>
                <td> {logInfo.nViolations} </td>
                <td> {logInfo.optStarted} </td>
                <td> {logInfo.optFound} </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>

      <Divider />
    </>
  );
};

export default LogInfo;
