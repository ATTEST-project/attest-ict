import React from 'react';
import { Row, Col } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';

import T41ScenarioDailyChart from 'app/modules/tools/WP4/T41/tractability-tool/results/charts/scenario-daily-chart';

interface T41ResultsChartInterface {
  rowData: any[];
  scenario: number;
  title: string;
  titleLongDescr?: string;
  yAxisTitle?: string;
}

const T41ResultsCharts = (props: T41ResultsChartInterface) => {
  const { rowData, scenario, title, titleLongDescr, yAxisTitle } = props;

  return (
    <>
      {(() => {
        switch (title) {
          case 'VOLT':
          case 'Crnt_PU':
          case 'Crnt_SI':
          case 'P_load':
          case 'Q_load':
          case 'Pg_max':
          case 'Qg_max':
          case 'Vlt_Viol':
          case 'Crnt_Viol':
            return (
              <>
                <Row>
                  <Col>
                    <T41ScenarioDailyChart
                      title={title}
                      scenario={scenario}
                      rowData={rowData}
                      titleLongDescr={titleLongDescr}
                      yAxisTitle={yAxisTitle}
                    />
                  </Col>
                </Row>
              </>
            );
            break;

          default:
            return <div />;
        }
      })()}
    </>
  );
};
export default T41ResultsCharts;
