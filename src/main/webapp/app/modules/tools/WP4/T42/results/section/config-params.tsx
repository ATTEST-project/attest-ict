import React from 'react';
import { Table } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import T42ConfigParamsI from 'app/modules/tools/WP4/T42/model/t42.config.params';
import { extractFileName } from 'app/shared/util/file-utils';
import TextTruncate from 'app/shared/components/text/text-truncate';

interface ConfigParamsInterface {
  parameters?: T42ConfigParamsI;
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
                <th className="hand">Current Time Period</th>
                <th className="hand">Matpower Network File</th>
                <th className="hand">Flex Devices Tech Char File</th>
                <th className="hand">Flexibility Devices States File</th>
                <th className="hand">Trans Activation File</th>
                <th className="hand">State Estimator CSV file</th>
                <th className="hand">Case Name</th>
                <th className="hand">Year</th>
                <th className="hand">Season</th>
                <th className="hand">Flexibility</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td> {parameters.current_time_period} </td>
                <td>
                  {' '}
                  <TextTruncate maxWidth={'550px'} text={extractFileName(parameters.matpower_network_file)} />
                </td>
                <td>
                  {' '}
                  <TextTruncate maxWidth={'550px'} text={extractFileName(parameters.flex_devices_tech_char_file)} />
                </td>
                <td>
                  {' '}
                  <TextTruncate maxWidth={'550px'} text={extractFileName(parameters.flexibity_devices_states_file)} />
                </td>
                <td>
                  {' '}
                  <TextTruncate maxWidth={'550px'} text={extractFileName(parameters.trans_activation_file)} />
                </td>
                <td>
                  {' '}
                  <TextTruncate maxWidth={'550px'} text={extractFileName(parameters.state_estimation_csv_file)} />
                </td>
                <td> {parameters.case_name} </td>
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
