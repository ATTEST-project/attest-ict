import React from 'react';
import Divider from 'app/shared/components/divider/divider';
import Plot from 'react-plotly.js';
import { RouteProps } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';

interface ChartProps {
  traces: any[];
  layout: any;
  config: any;
}

interface T33ConvergenceInterfaceProp {
  title: string;
  yAxis: string;
  rowData: any[];
}

const T33ConvergenceCharts = (props: T33ConvergenceInterfaceProp) => {
  const { title, yAxis, rowData } = props;

  const [charts, setCharts] = React.useState<ChartProps[]>([]);

  const setChart = () => {
    const finalCharts = [];
    const xAxisTitle = 'Iteration';
    const yAxisTitle = yAxis;
    const chartTitle = title;
    // Set Category  iteration
    const c1 = rowData?.map(r => r.Iteration);
    // set Series
    const series = ['Lower Bound', 'Upper Bound'];

    const traces = [];
    series.map(s => {
      const sData = rowData?.map(r => r[s]);
      const trace = {
        x: c1,
        y: sData,
        name: s,
        mode: 'lines+markers',
        type: 'scatter',
      };
      traces.push(trace);
    });

    const layout = {
      showlegend: true,
      title: chartTitle,
      xaxis: {
        title: {
          text: xAxisTitle,
          font: { size: 14 },
        },
      },
      yaxis: {
        title: {
          text: yAxisTitle,
          font: { size: 14 },
        },
      },
      legend: { xanchor: 'top', yanchor: 'right' },
    };

    const config = {
      showLink: false,
      scrollZoom: true,
      displaylogo: false,
      responsive: true,
    };

    finalCharts.push({ traces, layout, config });
    setCharts([...finalCharts]);
  };

  React.useEffect(() => {
    if (!rowData) {
      return;
    }
    setChart();
  }, [rowData]);

  return (
    <div className="section-with-border">
      <Divider />
      <div style={{ display: 'flex', justifyContent: 'space-around', alignItems: 'center' }}>
        {charts.map((chart, index) => (
          <div key={'convergence_' + index} style={{ width: 700, height: 500 }}>
            <Plot data={chart.traces} layout={chart.layout} config={chart.config} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default T33ConvergenceCharts;
