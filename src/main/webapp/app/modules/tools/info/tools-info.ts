const toolsInfo = {
  WP2: [
    {
      name: 'T2.5',
      description: 'Day Ahead Aggregator tool',
      to: '/tools/t251',
      type: 'all',
      ready: true,
    },
    {
      name: 'T2.5',
      description: 'Real Time Optimisation Tool',
      to: '/tools/t252',
      type: 'all',
      ready: true,
    },
    {
      name: 'T2.6',
      description: 'Market Simulator',
      to: '/tools/t26',
      type: 'all',
      ready: true,
    },
  ],
  WP3: [
    {
      name: 'T3.1',
      description: 'Optimisation Tool for distribution network planning',
      to: '/tools/t31',
      type: 'distribution',
      ready: true,
    },
    {
      name: 'T3.2',
      description: 'Optimisation Tool for transmission network planning',
      to: '/tools/t32',
      type: 'transmission',
      ready: true,
    },
    {
      name: 'T3.3',
      description: 'Optimisation tool for planning TSO/DSO shared technologies',
      to: '/wip',
      type: 'all',
      ready: false,
    },
  ],
  WP4: [
    {
      name: 'SGT',
      description: 'Scenario Generation Tool',
      to: '/tools/sgt',
      type: 'all',
      ready: true,
    },
    {
      name: 'T4.1',
      description: 'Tool for ancillary services activation in day ahead (DX)',
      to: '/tools/t41',
      type: 'distribution',
      ready: true,
    },
    {
      name: 'T4.2',
      description: 'Tool for ancillary services activation in real time (DX)',
      to: '/wip',
      type: 'distribution',
      ready: false,
    },
    {
      name: 'T4.3',
      description: 'State Estimator Tool',
      to: '/wip',
      type: 'all',
      ready: false,
    },
    {
      name: 'T4.4',
      description: 'Tool for ancillary services activation in day ahead (TX)',
      to: '/tools/t44',
      type: 'transmission',
      ready: true,
    },
    {
      name: 'T4.5',
      description: 'Tool for ancillary services activation in real time (TX)',
      to: '/wip',
      type: 'transmission',
      ready: false,
    },
    {
      name: 'T4.6',
      description: 'Tool For Online Dynamic Security Assessment',
      to: '/wip',
      type: 'all',
      ready: false,
    },
  ],
  WP5: [
    {
      name: 'T5.1',
      description: 'Assets Characterization and Monitoring Tools',
      to: '/tools/t51',
      type: 'all',
      ready: true,
    },
    {
      name: 'T5.2',
      description: 'Tool for the definition of condition indicators',
      to: '/tools/t52',
      type: 'all',
      ready: true,
    },
    {
      name: 'T5.3',
      description: 'Tool for the definition of smart asset management strategies',
      to: '/tools/t53',
      type: 'all',
      ready: true,
    },
  ],
};

export default toolsInfo;
