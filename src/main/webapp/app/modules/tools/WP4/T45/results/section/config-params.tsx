import React from 'react';
import { Table, Tooltip } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import T45ConfigParamsI from 'app/modules/tools/WP4/T45/model/t45.config.params';
import { extractFileName } from 'app/shared/util/file-utils';
import TextTruncate from 'app/shared/components/text/text-truncate';
import { defaultSeasonMap, defaultFlexibilityMap } from 'app/shared/model/tooltip-tools.model';

interface ConfigParamsInterface {
  parameters?: T45ConfigParamsI;
}

const ConfigParams = (props: ConfigParamsInterface) => {
  const { parameters } = props;

  const [showTooltipSeason, setShowTooltipSeason] = React.useState<boolean>(false);
  const [showTooltipFlexibility, setShowTooltipFlexibility] = React.useState<boolean>(false);

  const tooltipSeasonElements = defaultSeasonMap.map((item, index) => (
    <span key={index}>
      {`${item.key}: ${item.value}`}
      <br />
    </span>
  ));

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
                <td>
                  {' '}
                  <TextTruncate maxWidth={'550px'} text={extractFileName(parameters.matpower_network_file)} />{' '}
                </td>
                <td>
                  {' '}
                  <TextTruncate maxWidth={'550px'} text={extractFileName(parameters.flex_devices_tech_char_file)} />
                </td>
                <td>
                  {' '}
                  <TextTruncate maxWidth={'200px'} text={extractFileName(parameters.flexibity_devices_states_file)} />{' '}
                </td>
                <td>
                  {' '}
                  <TextTruncate maxWidth={'200px'} text={extractFileName(parameters.DA_curtailment_file)} />{' '}
                </td>
                <td>
                  {' '}
                  <TextTruncate maxWidth={'200px'} text={extractFileName(parameters.state_estimation_csv_file)} />{' '}
                </td>
                <td> {parameters.year} </td>
                <td id="param_season_id" onMouseEnter={() => setShowTooltipSeason(true)} onMouseLeave={() => setShowTooltipSeason(false)}>
                  {' '}
                  {parameters.season}{' '}
                </td>
                <td> {defaultFlexibilityMap.get(parameters.with_flex)} </td>
              </tr>
            </tbody>
          </Table>

          <Tooltip target="param_season_id" isOpen={showTooltipSeason}>
            {tooltipSeasonElements}
          </Tooltip>
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
