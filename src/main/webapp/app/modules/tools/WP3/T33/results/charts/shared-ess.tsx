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

interface T33SharedESSChartsProp {
  title: string;
  year: number;
  day: string;
  rowData: any[];
}

const T33SharedESSCharts = (props: T33SharedESSChartsProp) => {
  const { title, year, day, rowData } = props;
  const [charts, setCharts] = React.useState<ChartProps[]>([]);

  // Interface's node from TSO & DSO
  const getNodesId = (rowData): number[] => {
    const connectionNodesId: number[] = rowData.map(row => Number(row['Node ID']));
    // remove duplicate values
    const nodesId = Array.from(new Set(connectionNodesId));
    /* eslint-disable-next-line no-console */
    // console.log('nodesId: ', nodesId);
    return nodesId;
  };

  const isQuantityYearDayForESSOConnectionNodes = (row, quantity: string, nodes: number[]): boolean => {
    /* eslint-disable-next-line no-console */
    // console.log('Filter row by ', quantity, ' ', year, ' ', day, ' ', nodes, ' ESSO');
    return (
      nodes.includes(row['Node ID']) &&
      row.Operator === 'ESSO' &&
      row.Quantity === quantity &&
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
    /* eslint-disable-next-line no-console */
    // console.log('hourlyData: ', hourlyData);
    return hourlyData;
  };

  const setTrace = (row, time) => {
    const hourlyData = setHourlyData(row);
    const quantity = row['Quantity'];
    const nameSeries = 'NodeId: ' + row['Node ID'] + ',  ' + quantity.split(',')[0];

    const objTrace: Record<string, any> = {};
    objTrace.x = time;
    objTrace.y = hourlyData;
    objTrace.name = nameSeries;
    objTrace.mode = 'lines';
    objTrace.type = 'scatter';

    // show state of charge on the right
    if (quantity === 'SoC, [MVAh]') {
      (objTrace.yaxis = 'y2'), (objTrace.line = { dash: 'dashdot' });
    }
    return objTrace;
  };

  const setChart = () => {
    const finalCharts = [];
    const xAxisTitle = 'Time [hr:min]';
    const yAxisTitles = ['P, [MW]', 'SoC, [MVAh]']; // Active Power , State of Charge
    const chartTitle = title + '<br>  Year: ' + year + ' Day: ' + day;
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
    const nodesId = getNodesId(rowData);
    const traces = [];
    for (const yAxis of yAxisTitles) {
      const sharedEssValues = rowData?.filter(row => isQuantityYearDayForESSOConnectionNodes(row, yAxis, nodesId));
      for (const row of sharedEssValues) {
        const traceForNode = setTrace(row, time);
        traces.push(traceForNode);
      }
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
          text: yAxisTitles[0],
          font: { size: 14 },
        },
        // range: [0.85, 1.25]
      },
      yaxis2: {
        title: yAxisTitles[1],
        overlaying: 'y',
        side: 'right',
      },

      legend: {
        x: 1.1,
        y: 0.5,
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
          <div key={'sharedEss_' + year + '_' + day} style={{ width: 700, height: 500 }}>
            <Plot data={chart.traces} layout={chart.layout} config={chart.config} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default T33SharedESSCharts;
