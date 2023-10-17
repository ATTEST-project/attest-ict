import React from 'react';
import Divider from 'app/shared/components/divider/divider';
import Plot from 'react-plotly.js';
import { RouteProps } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';

interface T33CapacityChartsProp {
  title: string;
  yAxis: string;
  rowData: any[];
}

interface ChartProps {
  traces: any[];
  layout: any;
  config: any;
}

const T33CapacityCharts = (props: T33CapacityChartsProp) => {
  const { title, yAxis, rowData } = props;
  const [charts, setCharts] = React.useState<ChartProps[]>([]);

  const setTrace = (year, yAxisTitle) => {
    const powerData = rowData?.filter(x => x.Quantity === yAxisTitle);
    const nodes = powerData?.map(pw => pw.Node);
    const dataY = powerData?.map(pw => pw[year]);
    const traceY = {
      x: nodes,
      y: dataY,
      name: year,
      mode: 'lines+markers',
      type: 'bar',
    };

    /* eslint-disable-next-line no-console */
    console.log('traceY: ', traceY);
    return traceY;
  };

  const plotChart = () => {
    const finalCharts = [];
    const xAxisTitle = 'Node';
    const yAxisTitle = yAxis;
    const chartTitle = title;
    const traces = [];
    const years = [2020, 2030, 2040, 2050];

    years.map(year => {
      const trace = setTrace(year, yAxisTitle);
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
    plotChart();
  }, [rowData]);

  return (
    <div className="section-with-border">
      <Divider />
      <div style={{ display: 'flex', justifyContent: 'space-around', alignItems: 'center' }}>
        <div key="capacity" style={{ width: 700, height: 500 }}>
          {charts.map((chart, index) => (
            <div key={'capacity_' + yAxis + '_' + index} style={{ width: 700, height: 500 }}>
              <Plot data={chart.traces} layout={chart.layout} config={chart.config} />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default T33CapacityCharts;
