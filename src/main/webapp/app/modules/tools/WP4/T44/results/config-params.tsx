import React from 'react';
import { Table, Tooltip } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import { T44ConfigParamsI } from 'app/modules/tools/WP4/T44/model/t44.config.params';
import { defaultProfileMap, defaultT44ProblemsMap, defaultFlexibilityMap } from 'app/shared/model/tooltip-tools.model';
import { extractFileName } from 'app/shared/util/file-utils';

interface T44ConfigParametersInterface {
  parameters?: T44ConfigParamsI;
}

const T44ConfigParams = (props: T44ConfigParametersInterface) => {
  const { parameters } = props;

  const [showTooltipProfile, setShowTooltipProfile] = React.useState<boolean>(false);
  const [showTooltipProblem, setShowTooltipProblem] = React.useState<boolean>(false);

  const tooltipProfileElements = defaultProfileMap.map((item, index) => (
    <span key={index}>
      {`${item.key}: ${item.value}`}
      <br />
    </span>
  ));

  const tooltipProblemElements = defaultT44ProblemsMap.map((item, index) => (
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
                <th className="hand">Problem</th>
                <th className="hand">Year</th>
                <th className="hand">Profile</th>
                <th className="hand">Flexibility</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td> {parameters.case_name} </td>
                <td> {extractFileName(parameters.network_file)} </td>
                <td> {extractFileName(parameters.scenario_file)} </td>
                <td> {extractFileName(parameters.auxiliary_file)} </td>

                <td
                  id="param_problem_id"
                  onMouseEnter={() => setShowTooltipProblem(true)}
                  onMouseLeave={() => setShowTooltipProblem(false)}
                >
                  {parameters.problem}
                </td>

                <td> {parameters.year} </td>

                <td
                  id="param_profile_id"
                  onMouseEnter={() => setShowTooltipProfile(true)}
                  onMouseLeave={() => setShowTooltipProfile(false)}
                >
                  {parameters.profile}
                </td>
                <td> {defaultFlexibilityMap.get(parameters.flexibility)} </td>
              </tr>
            </tbody>
          </Table>
          <Tooltip target="param_problem_id" isOpen={showTooltipProblem}>
            {tooltipProblemElements}
          </Tooltip>

          <Tooltip target="param_profile_id" isOpen={showTooltipProfile}>
            {tooltipProfileElements}
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

export default T44ConfigParams;
