import React from 'react';
import { Row, Col } from 'reactstrap';
import T33CapacityCharts from 'app/modules/tools/WP3/T33/results/charts/capacity-charts';

const CapacityCostCharts = (props: any) => {
  const { title, rowData } = props;
  const quantities = ['Cost S, [m.u.]', 'Cost E, [m.u.]'];
  const titles = [' Power', ' Energy'];
  return (
    <>
      <Row>
        <Col>
          <T33CapacityCharts title={title + titles[0]} yAxis={quantities[0]} rowData={rowData} />
        </Col>
        <Col>
          <T33CapacityCharts title={title + titles[1]} yAxis={quantities[1]} rowData={rowData} />
        </Col>
      </Row>
    </>
  );
};

export default CapacityCostCharts;
