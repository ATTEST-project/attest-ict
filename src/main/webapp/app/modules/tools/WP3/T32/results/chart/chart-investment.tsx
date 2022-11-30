import React from 'react';
import case3_outputs from 'app/modules/tools/WP3/T31/results/sample-data/case3_outputs.json';
import Plot from 'react-plotly.js';
import { useAppSelector } from 'app/config/store';
import { IBranch } from 'app/shared/model/branch.model';
import axios from 'axios';

interface ChartProps {
  data: any[];
  layout: any;
}

const ChartInvestment = (props: any) => {
  const { investmentType } = props;

  const jsonResponse = useAppSelector(state => state.t32ToolResults.entity);
  const toolResponse = useAppSelector(state => state.t32ToolExecution.entity);
  const taskEntity = useAppSelector(state => state.task.entity);

  const networkId = taskEntity?.networkId || toolResponse?.args?.networkId;

  const [branches, setBranches] = React.useState<IBranch[]>(null);
  const [charts, setCharts] = React.useState<ChartProps[]>(null);

  const getInvestmentData = () => {
    const allInvestments = Object.fromEntries(Object.entries(jsonResponse).filter(([k, v]) => k.startsWith('Scenario ')));

    const years = Object.keys(Object.values(allInvestments)[0]).filter(k => k.startsWith('20'));

    let finalInvestments = {};
    for (const [k, v] of Object.entries(allInvestments)) {
      const obj = {
        [k]: {},
      };
      for (const year of years) {
        obj[k] = { [year]: allInvestments[k][year][investmentType], ...obj[k] };
      }
      finalInvestments = { ...obj, ...finalInvestments };
    }

    const finalCharts = [];
    for (const [k, v] of Object.entries(allInvestments)) {
      const data = [];
      const scenario = finalInvestments[k];
      for (const year of years) {
        const trace = {
          name: year,
          x: branches.map(branch => branch.id),
          y: scenario[year],
          type: 'bar',
        };
        data.push({ ...trace });
      }

      const layout = {
        showlegend: true,
        title: k,
        legend: {
          orientation: 'h',
          x: 0.35,
        },
      };

      finalCharts.push({ data, layout });
    }

    setCharts([...finalCharts]);
  };

  React.useEffect(() => {
    if (!networkId) {
      return;
    }
    axios.get<IBranch[]>('api/branches?networkId.equals=' + networkId).then(res => {
      setBranches(res.data);
    });
  }, []);

  React.useEffect(() => {
    if (!branches) {
      return;
    }
    getInvestmentData();
  }, [branches]);

  const config = {
    showLink: false,
    scrollZoom: true,
    displaylogo: false,
    responsive: true,
  };

  return (
    <div className="section-with-border">
      <h6>{investmentType}</h6>
      <div style={{ display: 'flex', justifyContent: 'space-around', alignItems: 'center' }}>
        {charts?.map((chart, index) => (
          <div key={index} style={{ width: 700, height: 500 }}>
            <Plot data={chart?.data} layout={chart?.layout} config={config} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default ChartInvestment;
