import React from 'react';
import { Button, Col, Input, List, Offcanvas, OffcanvasBody, OffcanvasHeader, Row, Table, Spinner, ButtonGroup } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import AllResults from 'app/modules/tools/WP4/T44/results/charts/section/all/all-results';
import Charts from 'app/shared/components/T41-44/results/charts/charts';

interface T44ChartInterface {
  costsData: any[];
}

const T44Charts = (props: T44ChartInterface) => {
  const { costsData } = props;
  const defaultSection = 'ALL';
  const [section, setSection] = React.useState<string>(defaultSection);

  const changeSection = (newSection: string) => {
    setSection(newSection);
  };

  const switchChart = section => {
    if (section === defaultSection) {
      return <AllResults />;
    } else {
      return <Charts section={section} type="scatter" />;
    }
  };

  return (
    <>
      <div className="section-with-border">
        <Table>
          <thead>
            <tr>
              <th>Cost Type</th>
              <th>Value</th>
              <th>Description</th>
            </tr>
          </thead>
          <tbody>
            {costsData?.map((cost, index) => (
              <tr key={index}>
                <td>{cost.costType}</td>
                <td>{cost.value}</td>
                <td>{cost.description}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>

      <Divider />
      <div className="section-with-border">
        <Button color="primary" variant="contained" onClick={() => changeSection('ALL')} active={section === 'ALL'}>
          ALL
        </Button>
        <Button color="primary" variant="contained" onClick={() => changeSection('ARP')} active={section === 'ARP'}>
          ARP
        </Button>
        <Button color="primary" variant="contained" onClick={() => changeSection('FL')} active={section === 'FL'}>
          FL
        </Button>
        <Button color="primary" variant="contained" onClick={() => changeSection('STR')} active={section === 'STR'}>
          STR
        </Button>
        <Button color="primary" variant="contained" onClick={() => changeSection('CURT')} active={section === 'CURT'}>
          CURT
        </Button>

        {switchChart(section)}
      </div>
    </>
  );
};

export default T44Charts;
