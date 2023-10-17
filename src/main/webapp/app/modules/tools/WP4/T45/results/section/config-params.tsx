import React from 'react';
import { Table } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import T45ConfigParamsI from 'app/modules/tools/WP4/T45/model/t45.config.params';
import { extractFileName } from 'app/shared/util/file-utils';

interface ConfigParamsInterface {
  parameters?: T45ConfigParamsI;
}

const ConfigParams = (props: ConfigParamsInterface) => {
  const { parameters } = props;

  const tableConfigParams = () => {
    return (
      <>
        <div className="section-with-border">
          <h5>Input Data Summary: </h5>
          <Table responsive className="table-responsive">
            <thead>
              <tr>
                <th className="hand">Case Name</th>
                <th className="hand">Current Time Period</th>
                <th className="hand">Output Distribution Bus</th>
                <th className="hand">Matpower Network File</th>
                <th className="hand">Flex Devices Tech Char File</th>
                <th className="hand">Flexibility Devices States File</th>
                <th className="hand">DA Curtailment File</th>
                <th className="hand">State Estimator CSV file</th>
                <th className="hand">Year</th>
                <th className="hand">Season</th>
                <th className="hand">Flexibility</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td> {parameters.case_name} </td>
                <td> {parameters.current_time_period} </td>
                <td> {parameters.output_distribution_bus} </td>
                <td> {extractFileName(parameters.matpower_network_file)} </td>
                <td> {extractFileName(parameters.flex_devices_tech_char_file)} </td>
                <td> {extractFileName(parameters.flexibity_devices_states_file)} </td>
                <td> {extractFileName(parameters.DA_curtailment_file)} </td>
                <td> {extractFileName(parameters.state_estimation_csv_file)} </td>
                <td> {parameters.year} </td>
                <td> {parameters.season} </td>
                <td> {parameters.with_flex} </td>
              </tr>
            </tbody>
          </Table>
        </div>
      </>
    );
  };

  return (
    <>
      <Divider />
      {tableConfigParams()}
    </>
  );
};

export default ConfigParams;
