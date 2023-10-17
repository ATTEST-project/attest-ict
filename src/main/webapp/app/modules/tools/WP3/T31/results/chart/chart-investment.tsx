import React from 'react';
import case3_outputs from 'app/modules/tools/WP3/T31/results/sample-data/case3_outputs.json';
import Plot from 'react-plotly.js';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import axios from 'axios';
import { IBranch } from 'app/shared/model/branch.model';
import { IBus } from 'app/shared/model/bus.model';

interface ChartProps {
  data: any[];
  layout: any;
}

interface ChartDescriptionI {
  investmentType: string;
  xAxisTitle?: string;
  yAxisTitle?: string;
}

const ChartInvestment = (props: ChartDescriptionI) => {
  const { investmentType, xAxisTitle, yAxisTitle } = props;

  const jsonResponse = useAppSelector(state => state.t31ToolResults.entity);
  const toolResponse = useAppSelector(state => state.t31ToolExecution.entity);
  const taskEntity = useAppSelector(state => state.task.entity);
  const [charts, setCharts] = React.useState<ChartProps[]>(null);
  const networkId = taskEntity?.networkId || toolResponse?.args?.networkId;
  const [branches, setBranches] = React.useState<IBranch[]>(null);
  const [buses, setBuses] = React.useState<IBus[]>(null);

  const setXLabel = () => {
    if (investmentType === 'Branch investment (MVA)') {
      return branches.map(branch => 'br_' + branch.fbus + '_' + branch.tbus);
    } else {
      return buses.map(bus => 'bus_' + bus.busNum);
    }
  };

  const getInvestmentData = xLabel => {
    const allInvestments = Object.fromEntries(Object.entries(jsonResponse).filter(([k, v]) => k.startsWith('Scenario ')));

    const years = Object.keys(Object.values(allInvestments)[0]).filter(k => k.startsWith('20'));

    let finalInvestments = {};
    for (const [k, v] of Object.entries(allInvestments)) {
      const obj = {
        [k]: {},
      };
      for (let i = 0; i < years.length; ++i) {
        obj[k] = { [years[i]]: allInvestments[k][years[i]][investmentType], ...obj[k] };
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
          x: xLabel,
          y: scenario[year],
          type: 'bar',
        };
        data.push({ ...trace });
      }

      const layout = {
        showlegend: true,
        title: k,
        xaxis: {
          title: {
            text: xAxisTitle,
            font: { size: 12 },
          },
        },
        yaxis: {
          title: {
            text: yAxisTitle,
            font: { size: 12 },
          },
        },
        legend: {
          xanchor: 'top',
          yanchor: 'right',
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

    if (investmentType === 'Branch investment (MVA)') {
      axios.get<IBranch[]>('api/branchesNoPagination?networkId.equals=' + networkId).then(res => {
        setBranches(res.data);
      });
    } else {
      axios.get<IBus[]>('api/busesLessPagination?networkId.equals=' + networkId).then(res => {
        setBuses(res.data);
      });
    }
  }, []);

  React.useEffect(() => {
    if (!branches && !buses) {
      return;
    }
    getInvestmentData(setXLabel());
  }, [branches, buses]);

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
