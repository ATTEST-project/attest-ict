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

interface T41ScenarioDailyChartProp {
  title: string;
  scenario: number;
  rowData: any[];
  titleLongDescr?: string;
  yAxisTitle?: string;
}

const T41ScenarioDailyChart = (props: T41ScenarioDailyChartProp) => {
  const { title, scenario, rowData, titleLongDescr, yAxisTitle } = props;
  const [charts, setCharts] = React.useState<ChartProps[]>([]);

  const yAxisDescr = () => {
    return yAxisTitle ? yAxisTitle : title;
  };

  const isDataForScenario = row => {
    /* eslint-disable-next-line no-console */
    // console.log('Filter results by ', scenario);
    return row['Scen'] === scenario;
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

  const setTrace = (row, xlabels) => {
    const hourlyData = setHourlyData(row);
    const nameSeries = 'Id: ' + row['ID'];
    return {
      x: xlabels,
      y: hourlyData,
      name: nameSeries,
      mode: 'lines',
      type: 'scatter',
    };
  };

  const setChart = () => {
    const finalCharts = [];
    const xAxisTitle = 'Time [hr:min]';
    const yAxisTitle = yAxisDescr();
    const chartTitle = ' Scen:' + scenario + ' - ' + (titleLongDescr ? titleLongDescr : title);
    /* eslint-disable-next-line no-console */
    // console.log('rowData: ', rowData);
    const data = rowData?.filter(row => isDataForScenario(row));
    // set Category
    const xLabels = [
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
    for (const row of data.values()) {
      const traceForScenario = setTrace(row, xLabels);
      traces.push(traceForScenario);
    }

    const layout = {
      autosize: false,
      width: 1500,
      height: 500,
      showlegend: true,
      title: chartTitle,
      xaxis: {
        automargin: true,
        range: [0, 24],
        zeroline: true,
        showline: true,
        title: {
          text: xAxisTitle,
          font: { size: 14 },
        },
      },
      yaxis: {
        automargin: true,
        title: {
          text: yAxisTitle,
          font: { size: 14 },
        },
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
  }, [rowData, scenario]);

  return (
    <div className="section-with-border">
      <Divider />
      <div style={{ display: 'flex', justifyContent: 'space-around', alignItems: 'center' }}>
        {charts.map((chart, index) => (
          <Plot key={'snc_' + scenario} data={chart.traces} layout={chart.layout} config={chart.config} />
        ))}
      </div>
    </div>
  );
};

export default T41ScenarioDailyChart;
