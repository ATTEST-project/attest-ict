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

interface T33ESSSecondaryReserveChartsProp {
  title: string;
  day: string;
  rowData: any[];
}

const T33ESSSecondaryReserveCharts = (props: T33ESSSecondaryReserveChartsProp) => {
  const { title, day, rowData } = props;

  const [charts, setCharts] = React.useState<ChartProps[]>([]);

  const isSelectedDay = (row): boolean => {
    return row.timeSeriesHour.Day === day;
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
    const type = row.Type;
    const nameSeries = row.timeSeriesHour.Year + ' ' + type;
    const objTrace: Record<string, any> = {};
    objTrace.x = time;
    objTrace.y = hourlyData;
    objTrace.name = nameSeries;
    objTrace.mode = 'lines';
    objTrace.type = 'scatter';
    if (type === 'Downward, [MW]') objTrace.line = { dash: 'dashdot' };
    return objTrace;
  };

  const setChart = () => {
    const finalCharts = [];
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
    const xAxisTitle = 'Time [hr:min]';
    const yAxisTitle = 'Active Power, [MW]'; // Active Power
    const chartTitle = title + ' - Day: ' + day;
    const secondReserveBandsByDay = rowData?.filter(row => isSelectedDay(row));

    const traces = [];
    for (const row of secondReserveBandsByDay) {
      const traceBands = setTrace(row, time);
      traces.push(traceBands);
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
    if (rowData) {
      setChart();
    } else return;
  }, [rowData, day]);

  return (
    <div className="section-with-border">
      <Divider />
      <div style={{ display: 'flex', justifyContent: 'space-around', alignItems: 'center' }}>
        {charts.map((chart, index) => (
          <div key={'secondaryReserve_' + day} style={{ width: 900, height: 500 }}>
            <Plot data={chart.traces} layout={chart.layout} config={chart.config} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default T33ESSSecondaryReserveCharts;
