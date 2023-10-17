import React from 'react';
import { Table, Tooltip } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import T41ConfigParamsI from 'app/modules/tools/WP4/T41/tractability-tool/model/t41.config.params';
import { defaultSeasonMap, defaultFlexibilityMap } from 'app/shared/model/tooltip-tools.model';
import { extractFileName } from 'app/shared/util/file-utils';

interface T41ConfigParametersInterface {
  parameters?: T41ConfigParamsI;
}

const T41ConfigParams = (props: T41ConfigParametersInterface) => {
  const { parameters } = props;

  const [showTooltip, setShowTooltip] = React.useState<boolean>(false);

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
                <th className="hand">Network file</th>
                <th className="hand">Scenario file</th>
                <th className="hand">Auxiliary file</th>
                <th className="hand">Year</th>
                <th className="hand">Season</th>
                <th className="hand">Flexibility</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td> {parameters.case_name} </td>
                <td> {extractFileName(parameters.network_file)} </td>
                <td> {extractFileName(parameters.scenario_file)} </td>
                <td> {extractFileName(parameters.auxiliary_file)} </td>
                <td> {parameters.year} </td>
                <td id="param_season_id" onMouseEnter={() => setShowTooltip(true)} onMouseLeave={() => setShowTooltip(false)}>
                  {parameters.season}
                </td>
                <td> {defaultFlexibilityMap.get(parameters.with_flex)} </td>
              </tr>
            </tbody>
          </Table>
        </div>

        <Tooltip target="param_season_id" isOpen={showTooltip}>
          {tooltipSeasonElements}
        </Tooltip>
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

export default T41ConfigParams;
