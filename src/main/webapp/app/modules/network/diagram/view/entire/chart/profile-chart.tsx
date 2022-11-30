import React from 'react';
import Plot from 'react-plotly.js';
import genJson from './chart_gen_all_seasons.json';
import loadJson from './chart_load_all_seasons.json';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { generateChartAllSeasonsOrDays } from 'app/entities/network/network-sld-entire-chart.reducer';
import { INetwork } from 'app/shared/model/network.model';
import { NetworkSLDChartModel } from 'app/shared/model/network-sld-chart.model';

interface ProfileChartProps {
  isFullscreen: boolean;
  component: string;
  network: INetwork;
}

const ProfileChart = (props: ProfileChartProps) => {
  const { isFullscreen, component, network } = props;

  // const isLoad = component.startsWith('load_');
  const fixedComponent = component.includes('#') ? component.substring(0, component.indexOf('#')) : component;

  const dispatch = useAppDispatch();

  const logAllProps = () => {
    /* eslint-disable-next-line no-console */
    console.log('Props: ', props);
  };

  const [graphSize, setGraphSize] = React.useState({
    width: 435,
    height: 350,
  });

  const [chart, setChart] = React.useState({
    data: [],
    layout: {},
  });

  const chartEntity = useAppSelector(state => state.networkSLDChart.entity);
  const loadingChartEntity = useAppSelector(state => state.networkSLDChart.loading);

  function handleResize() {
    setGraphSize({
      width: document.documentElement.clientWidth - 50,
      height: document.documentElement.clientHeight - 100,
    });
  }

  function handleChartResize() {
    const popover = document.querySelector('.popover');
    const popoverStyle = window.getComputedStyle(popover);
    const popoverWidth = parseInt(popoverStyle.width, 10);
    const popoverHeight = parseInt(popoverStyle.height, 10);
    setGraphSize({
      width: popoverWidth - 50,
      height: popoverHeight - 100,
    });
  }

  const callback = function (mutationList, observer) {
    for (const mutation of mutationList) {
      /* if (mutation.type === 'attributes' && mutation.attributeName === 'style') {
        handleChartResize()
      } */
      handleChartResize();
    }
  };

  React.useEffect(() => {
    logAllProps();

    if (isFullscreen) {
      setGraphSize({
        width: document.documentElement.clientWidth - 50,
        height: document.documentElement.clientHeight - 100,
      });
      window.addEventListener('resize', handleResize);
    } else {
      const popover = document.querySelector('.popover');
      new MutationObserver(callback).observe(popover, { attributes: true, attributeFilter: ['style'], childList: false, subtree: false });
      // new ResizeObserver(handleChartResize).observe(popover);
    }

    const busNum = parseInt(fixedComponent.substring(fixedComponent.indexOf('_') + 1), 10);
    const componentType = fixedComponent.substring(0, fixedComponent.indexOf('_'));
    dispatch(generateChartAllSeasonsOrDays({ networkId: network.id, busNum, type: componentType, period: 'season' }));
  }, []);

  React.useEffect(() => {
    if ((typeof chartEntity === 'object' && Object.keys(chartEntity).length === 0) || typeof chartEntity === 'string') {
      return;
    }
    getChartData(chartEntity);
  }, [chartEntity]);

  const getChartData = (dataProfile: NetworkSLDChartModel) => {
    const data = [];

    for (let i = 0; i < dataProfile.datasets.length; ++i) {
      const trace = {
        x: dataProfile.options.xaxis.labels,
        y: dataProfile.datasets[i].values,
        name: dataProfile.datasets[i].label,
        mode: 'lines',
        type: 'scatter',
      };
      data.push(trace);
    }
    const layout = {
      showlegend: true,
      title: dataProfile.options.title,
      xaxis: {
        title: {
          text: dataProfile.options.xaxis.title,
          font: {
            size: 14,
          },
        },
      },
      yaxis: {
        title: {
          text: dataProfile.options.yaxis.title,
          font: {
            size: 14,
          },
        },
      },
    };
    setChart({
      data,
      layout,
    });
  };

  const config = {
    showLink: false,
    scrollZoom: true,
    displaylogo: false,
    responsive: true,
  };

  return (
    <div style={{ width: '100%', height: '100%' }}>
      {chart.data.length > 0 ? (
        <Plot data={chart.data} layout={{ ...chart.layout, width: graphSize.width, height: graphSize.height }} config={config} />
      ) : (
        !loadingChartEntity && (
          <div>
            <span>No data to display</span>
          </div>
        )
      )}
    </div>
  );
};

export default ProfileChart;
