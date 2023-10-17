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

interface T33InterfacePfChartsProp {
  title: string;
  nodeId: number;
  year: number;
  day: string;
  rowData: any[];
}

const T33InterfacePfCharts = (props: T33InterfacePfChartsProp) => {
  const { title, nodeId, year, day, rowData } = props;
  /* eslint-disable-next-line no-console */
  // console.log(' T33InterfacePfCharts : ', { nodeId }, { day });
  const [charts, setCharts] = React.useState<ChartProps[]>([]);

  const isOperatorQuantityForNodeId = (row, quantity, operator) => {
    /* eslint-disable-next-line no-console */
    // console.log('Filter row by ', quantity, ' ', operator, ' ', nodeId, ' ', day, ' ', year);
    return (
      row['Node ID'] === nodeId &&
      row.Quantity === quantity &&
      row.Operator === operator &&
      row.timeSeriesHour.Year === year &&
      row.timeSeriesHour.Day === day
    );
  };

  const getHourlyData = (quantity, operator) => {
    const activePowerForNode = rowData?.filter(row => isOperatorQuantityForNodeId(row, quantity, operator));
    /* eslint-disable-next-line no-console */
    // console.log('activePowerForNode: ', activePowerForNode);
    const hourlyData =
      activePowerForNode === undefined || activePowerForNode.length === 0
        ? []
        : Object.keys(activePowerForNode[0].timeSeriesHour)
            .filter(key => key.startsWith('t'))
            .map(key => activePowerForNode[0].timeSeriesHour[key]);
    /* eslint-disable-next-line no-console */
    // console.log('hourlyData: ', hourlyData);
    return hourlyData;
  };

  const setTraceActivePower = (operator, time) => {
    const title = operator + ' Active Power';
    const quantity = 'P, [MW]';
    const hourlyData = getHourlyData(quantity, operator);
    const traceP = {
      x: time,
      y: hourlyData,
      name: title,
      mode: 'lines+markers',
      type: 'scatter',
    };
    /* eslint-disable-next-line no-console */
    // console.log('traceP: ', traceP);
    return traceP;
  };

  const setTraceReactivePower = (operator, time) => {
    const title = operator + ' Reactive Power';
    const quantity = 'Q, [MVAr]';
    const hourlyData = getHourlyData(quantity, operator);
    const traceQ = {
      x: time,
      y: hourlyData,
      name: title,
      yaxis: 'y2',
      mode: 'lines+markers',
      type: 'scatter',
      line: {
        dash: 'dashdot',
      },
    };
    /* eslint-disable-next-line no-console */
    // console.log('traceQ: ', traceQ);
    return traceQ;
  };

  const setChart = () => {
    const finalCharts = [];
    const xAxisTitle = 'Time [hr:min]';
    const yAxisTitle = 'AP, [MW]'; // Active Power
    const y2AxisTitle = 'RP, [MVAr]'; // Reactive Power Q, [MVAr]
    const chartTitle = title + '<br>  Year:' + year + ' Day: ' + day + '  NodeId: ' + nodeId + '<br>';
    //  const time = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24];
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
    traces.push(setTraceActivePower('TSO', time));
    traces.push(setTraceReactivePower('TSO', time));
    traces.push(setTraceActivePower('DSO', time));
    traces.push(setTraceReactivePower('DSO', time));

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
      },
      yaxis2: {
        title: y2AxisTitle,
        overlaying: 'y',
        side: 'right',
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
  }, [rowData, day, nodeId]);

  return (
    <div className="section-with-border">
      <Divider />
      <div style={{ display: 'flex', justifyContent: 'space-around', alignItems: 'center' }}>
        {charts.map((chart, index) => (
          <div key={'ipf_' + nodeId + '_' + day + '_' + year} style={{ width: 700, height: 500 }}>
            <Plot data={chart.traces} layout={chart.layout} config={chart.config} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default T33InterfacePfCharts;
