import React from 'react';
import { Row, Col } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import T33CapacityCharts from 'app/modules/tools/WP3/T33/results/charts/capacity-charts';
import CapacityPlanCharts from 'app/modules/tools/WP3/T33/results/charts/capacity-plan';
import CapacityCostCharts from 'app/modules/tools/WP3/T33/results/charts/capacity-cost';
import T33ConvergenceCharts from 'app/modules/tools/WP3/T33/results/charts/convergence';
import T33InterfacePfCharts from 'app/modules/tools/WP3/T33/results/charts/interface-power-flow';
import T33NetVMagCharts from 'app/modules/tools/WP3/T33/results/charts/network-voltage-mag';
import T33VMagCharts from 'app/modules/tools/WP3/T33/results/charts/interface-voltage-mag';
import T33ESSSecondaryReserveCharts from 'app/modules/tools/WP3/T33/results/charts/ess-secondary-reserve';
import T33SharedESSCharts from 'app/modules/tools/WP3/T33/results/charts/shared-ess';

interface T33ChartsInterface {
  rowData: any[];
  day: string;
  nodeId: number;
  pageTitle: string;
}

const T33Charts = (props: T33ChartsInterface) => {
  const { rowData, day, nodeId, pageTitle } = props;

  return (
    <>
      {(() => {
        switch (pageTitle) {
          case 'Unitary Investment Cost':
            return <CapacityCostCharts title={'Cost'} rowData={rowData} />;
          case 'Investment Plan':
            return <CapacityPlanCharts title={'Capacity'} rowData={rowData} />;
          case 'Converge Characteristic':
            return <T33ConvergenceCharts title={'Evolution of upped and lower bonds'} yAxis="Cost [NPV Mm.u.]" rowData={rowData} />;
          case 'Expected Interface Power Flow':
            return (
              <>
                <Row>
                  <Col>
                    <T33InterfacePfCharts title={'Interface Power Flow'} nodeId={nodeId} day={day} year={2020} rowData={rowData} />
                  </Col>
                  <Col>
                    <T33InterfacePfCharts title={'Interface Power Flow'} nodeId={nodeId} day={day} year={2030} rowData={rowData} />
                  </Col>
                </Row>
                <Divider />
                <Row>
                  <Col>
                    <T33InterfacePfCharts title={'Interface Power Flow'} nodeId={nodeId} day={day} year={2040} rowData={rowData} />
                  </Col>
                  <Col>
                    <T33InterfacePfCharts title={'Interface Power Flow'} nodeId={nodeId} day={day} year={2050} rowData={rowData} />
                  </Col>
                </Row>
              </>
            );
          case 'Expected Interface Voltage Magnitude':
            return (
              <>
                <Row>
                  <Col>
                    <T33VMagCharts title={'Interface Voltage Magnitude'} day={day} year={2020} rowData={rowData} />
                  </Col>
                  <Col>
                    <T33VMagCharts title={'Interface Voltage Magnitude'} day={day} year={2030} rowData={rowData} />
                  </Col>
                </Row>
                <Divider />
                <Row>
                  <Col>
                    <T33VMagCharts title={'Interface Voltage Magnitude'} day={day} year={2040} rowData={rowData} />
                  </Col>
                  <Col>
                    <T33VMagCharts title={'Interface Voltage Magnitude'} day={day} year={2050} rowData={rowData} />
                  </Col>
                </Row>
              </>
            );
          case 'Network Expected Voltage Magnitude':
            return (
              <>
                <Row>
                  <Col>
                    <T33NetVMagCharts
                      title={'TX Network Voltage Magnitude'}
                      connectionNodeId={nodeId}
                      day={day}
                      year={2020}
                      rowData={rowData}
                    />
                  </Col>
                  <Col>
                    <T33NetVMagCharts
                      title={'TX Network Voltage Magnitude'}
                      connectionNodeId={nodeId}
                      day={day}
                      year={2030}
                      rowData={rowData}
                    />
                  </Col>
                </Row>
                <Divider />
                <Row>
                  <Col>
                    <T33NetVMagCharts
                      title={'TX Network Voltage Magnitude'}
                      connectionNodeId={nodeId}
                      day={day}
                      year={2040}
                      rowData={rowData}
                    />
                  </Col>
                  <Col>
                    <T33NetVMagCharts
                      title={'TX Network Voltage Magnitude'}
                      connectionNodeId={nodeId}
                      day={day}
                      year={2050}
                      rowData={rowData}
                    />
                  </Col>
                </Row>
              </>
            );
            break;
          case 'Shared ESS - Secondary Reserve':
            return (
              <>
                <Row>
                  <Col>
                    <T33ESSSecondaryReserveCharts title={'Secondary Reserve bands supplied by ESSO'} day={day} rowData={rowData} />
                  </Col>
                </Row>
              </>
            );
            break;
          case 'Shared ESS - Active Power and State of Charge':
            return (
              <>
                <Row>
                  <Col>
                    <T33SharedESSCharts title={'ESSO Active Power and SoC'} day={day} year={2020} rowData={rowData} />
                  </Col>
                  <Col>
                    <T33SharedESSCharts title={'ESSO Active Power and SoC'} day={day} year={2030} rowData={rowData} />
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <T33SharedESSCharts title={'ESSO Active Power and SoC'} day={day} year={2040} rowData={rowData} />
                  </Col>
                  <Col>
                    <T33SharedESSCharts title={'ESSO Active Power and SoC'} day={day} year={2050} rowData={rowData} />
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
export default T33Charts;
