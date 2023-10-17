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

interface T33VMagChartsProp {
  title: string;
  year: number;
  day: string;
  rowData: any[];
}

const T33VMagCharts = (props: T33VMagChartsProp) => {
  const { title, year, day, rowData } = props;
  const [charts, setCharts] = React.useState<ChartProps[]>([]);

  // Interface's node from TSO & DSO
  const getNodesId = (rowData, yAxisTitle: string): number[] => {
    const connectionNodesId: number[] = rowData
      ?.filter(row => row['Connection Node ID'] !== '-' && row.Quantity === yAxisTitle && row.Operator === 'DSO')
      .map(r => Number(r['Connection Node ID']));
    // remove duplication
    const nodesId = Array.from(new Set(connectionNodesId));
    return nodesId;
  };

  const isQuantityOperatorYearDayForConnectionNodes = (row, quantity: string, operator: string, nodes: number[]): boolean => {
    return (
      nodes.includes(row['Node ID']) &&
      row.Quantity === quantity &&
      row.Operator === operator &&
      row.timeSeriesHour.Year === year &&
      row.timeSeriesHour.Day === day
    );
  };

  const setHourlyData = row => {
    const hourlyData =
      row === undefined || row.length === 0
        ? []
        : Object.keys(row.timeSeriesHour)
            .filter(key => key.startsWith('t'))
            .map(key => row.timeSeriesHour[key]);
    return hourlyData;
  };

  const setTrace = (row, time) => {
    const hourlyData = setHourlyData(row);
    const nameSeries = 'NodeId: ' + row['Node ID'];
    return {
      x: time,
      y: hourlyData,
      name: nameSeries,
      mode: 'lines',
      type: 'scatter',
    };
  };

  const setChart = () => {
    const finalCharts = [];
    const xAxisTitle = 'Time [hr:min]';
    const yAxisTitle = 'Vmag, [p.u.]'; // Voltage Mangnitude
    const chartTitle = title + ' -Year: ' + year + ' Day: ' + day;
    const operator = 'TSO';
    const nodesId = getNodesId(rowData, yAxisTitle);
    const vMagForNodes = rowData?.filter(row => isQuantityOperatorYearDayForConnectionNodes(row, yAxisTitle, operator, nodesId));

    /* eslint-disable-next-line no-console */
    // console.log('vMagForNodes: ', vMagForNodes);

    // const time = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24];
    const time = [
      '01:00',
      '02:00',
      '03:00',
      '04:00',
      '05:00',
      '06:00',
      '07:00',
      '08:00',
      '09:00',
      '10:00',
      '11:00',
      '12:00',
      '13:00',
      '14:00',
      '15:00',
      '16:00',
      '17:00',
      '18:00',
      '19:00',
      '20:00',
      '21:00',
      '22:00',
      '23:00',
      '24:00',
    ];
    const traces = [];

    for (const row of vMagForNodes) {
      const traceForDsoNode = setTrace(row, time);
      traces.push(traceForDsoNode);
    }

    const layout = {
      showlegend: true,
      title: chartTitle,
      xaxis: {
        range: [0, 24],
        zeroline: true,
        showline: true,
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
        // range: [0.85, 1.25]
      },

      legend: {
        xanchor: 'top',
        yanchor: 'right',
      },
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
  }, [rowData, day]);

  return (
    <div className="section-with-border">
      <Divider />
      <div style={{ display: 'flex', justifyContent: 'space-around', alignItems: 'center' }}>
        {charts.map((chart, index) => (
          <div key={'ivmg_' + day + '_' + year} style={{ width: 700, height: 500 }}>
            <Plot data={chart.traces} layout={chart.layout} config={chart.config} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default T33VMagCharts;
