import React from 'react';
import case3_outputs from '../../sample-data/case3_outputs.json';
import pt2020_outputs from '../../sample-data/investment_result_PT_Transmission_Network_PT_2020_ods_pt1.json';
import { Button, Collapse, Input, Table } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import Plot from 'react-plotly.js';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppSelector } from 'app/config/store';

interface ChartProps {
  data: any[];
  layout: any;
}

const TotalCost = (props: any) => {
  /* eslint-disable-next-line no-console */
  console.log('T31 Total Cost start ');

  const jsonResponse = useAppSelector(state => state.t31ToolResults.entity);

  /* eslint-disable-next-line no-console */
  console.log('T31 jsonResponse ', jsonResponse);

  const [showChart, setShowChart] = React.useState<boolean>(false);

  const [chart, setChart] = React.useState<ChartProps>(null);

  const getTotalCostData = () => {
    const totalCosts = Object.entries(jsonResponse)
      .filter(([k, v]) => k.startsWith('Scenario '))
      .map(([k, v]) => v);
    /* eslint-disable-next-line no-console */
    console.log('Total Costs: ', totalCosts);
    const data = [];
    for (let i = 0; i < totalCosts.length; ++i) {
      const trace = {
        name: 'Scenario ' + (i + 1),
        x: [
          totalCosts[i]['Total investment cost (EUR-million)'],
          totalCosts[i]['Flexibility investment cost (EUR-million)'],
          totalCosts[i]['Net Present Operation Cost (EUR-million)'],
        ],
        y: ['Investment', 'Flex', 'Net Present Op'],
        type: 'bar',
        orientation: 'h',
      };
      data.push(trace);
    }

    const layout = {
      showlegend: true,
      title: 'Total Cost (EUR-million)',
      legend: {
        orientation: 'h',
        x: 0.35,
      },
      yaxis: {
        tickfont: {
          size: 10,
        },
        ticksuffix: ' ',
      },
    };

    setChart({ data, layout });
  };

  React.useEffect(() => {
    getTotalCostData();
  }, []);

  const config = {
    showLink: false,
    scrollZoom: true,
    displaylogo: false,
    responsive: true,
  };

  return (
    <div className="section-with-border">
      <h6>Total Cost (Euro-million)</h6>
      <Divider />
      <Table>
        <thead>
          <tr>
            <th>Type</th>
            <th>Scenario 1</th>
            <th>Scenario 2</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Investment</td>
            <td>{jsonResponse['Scenario 1']['Total investment cost (EUR-million)']}</td>
            <td>{jsonResponse['Scenario 2']['Total investment cost (EUR-million)']}</td>
          </tr>
          <tr>
            <td>Flex</td>
            <td>{jsonResponse['Scenario 1']['Flexibility investment cost (EUR-million)']}</td>
            <td>{jsonResponse['Scenario 2']['Flexibility investment cost (EUR-million)']}</td>
          </tr>
          <tr>
            <td>Net Present Op</td>
            <td>{jsonResponse['Scenario 1']['Net Present Operation Cost (EUR-million)']}</td>
            <td>{jsonResponse['Scenario 2']['Net Present Operation Cost (EUR-million)']}</td>
          </tr>
        </tbody>
      </Table>
      <div style={{ marginRight: 10, textAlign: 'right' }}>
        <Button color="light" outline size="sm" style={{ boxShadow: 'none' }} onClick={() => setShowChart(!showChart)}>
          <span>{showChart ? 'Hide Chart ' : 'Show Chart '}</span>
          <FontAwesomeIcon icon="angle-down" style={showChart && { transform: 'rotate(180deg)' }} />
        </Button>
      </div>
      <Collapse isOpen={showChart}>
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ width: 700, height: 500 }}>
            <Plot data={chart?.data} layout={chart?.layout} config={config} />
          </div>
        </div>
      </Collapse>
    </div>
  );
};

export default TotalCost;
