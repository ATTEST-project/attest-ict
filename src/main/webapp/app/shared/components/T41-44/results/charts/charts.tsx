import React from 'react';
import Divider from 'app/shared/components/divider/divider';
import Plot from 'react-plotly.js';
import { RouteProps } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';

interface ChartsParams {
  location?: any;
  section: string;
  type: string;
  sampleResponse?: any;
}

interface ChartProps {
  data: any[];
  layout: any;
}

const Charts = (props: ChartsParams) => {
  const { location, section, type, sampleResponse } = props;

  const chartsResponse = useAppSelector(state => state.toolsResults.entity);

  const [pageTitle, setPageTitle] = React.useState<string>('');

  const [charts, setCharts] = React.useState<ChartProps[]>([]);

  const getChartData = () => {
    const allCharts = chartsResponse?.charts?.filter(x => x.section.startsWith(section));

    const finalCharts = [];
    for (let i = 0; i < allCharts?.length; ++i) {
      const data = [];
      for (let j = 0; j < allCharts[i].datasets.length; ++j) {
        const trace = {
          x: allCharts[i].options.xaxis.labels,
          y: allCharts[i].datasets[j].values,
          name: allCharts[i].datasets[j].label,
          type,
        };
        data.push(trace);
      }
      const layout = {
        showlegend: true,
        title: allCharts[i].options.title,
        xaxis: {
          title: {
            text: allCharts[i].options.xaxis.title,
            font: {
              size: 14,
            },
          },
        },
        yaxis: {
          title: {
            text: allCharts[i].options.yaxis.title,
            font: {
              size: 14,
            },
          },
        },
      };

      finalCharts.push({ data, layout });
    }
    setCharts([...finalCharts]);

    setPageTitle(allCharts?.[0]?.group);
  };

  React.useEffect(() => {
    getChartData();
  }, [chartsResponse, section]);

  const config = {
    showLink: false,
    scrollZoom: true,
    displaylogo: false,
    responsive: true,
  };

  const renderCharts = charts => {
    if (charts.length === 0) {
      return (
        <div className="section-with-border">
          <div>{section}</div>
          <div> No Data To Display</div>
        </div>
      );
    } else {
      return (
        <div className="section-with-border">
          <div>{pageTitle}</div>
          <Divider />
          <div style={{ display: 'flex', justifyContent: 'space-around', alignItems: 'center' }}>
            {charts.map((chart, index) => (
              <div key={index} style={{ width: 700, height: 500 }}>
                <Plot data={chart.data} layout={chart.layout} config={config} />
              </div>
            ))}
          </div>
        </div>
      );
    }
  };

  return <>{renderCharts(charts)}</>;
};

export default Charts;
