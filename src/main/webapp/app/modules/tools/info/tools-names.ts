export const TOOLS_NAMES = {
  T25_DAY_AHEAD_TOOL: 'T25_dayahead',
  T25_REAL_TIME_TOOL: 'T25_realtime',
  T26_MARKET_SIMUL: 'T26_marketSimul',
  T41_WIND_AND_PV: 'T41_windpv',
  T41_STOCHASTIC_MP_ACOPF: 'T41_stochasticmp',
  T41_TRACTABILITY: 'T41_Tractability_Tool',
  T42_AS_REAL_TIME_DX: 'T42_asRealTimeDx',
  T43_STATE_ESTIMATOR: 'T43_stateEstimator',
  T44_AS_DAY_HEAD_TX: 'T44_asDayheadTx',
  T45_AS_REAL_TIME_TX: 'T45_asRealTimeTx',
  T46_ODSA: 'T46_odsa',
  T31_OPT_TOOL_DX: 'T31_optToolDx',
  T32_OPT_TOOL_TX: 'T32_optToolTx',
  T33_OPT_TOOL_PLAN_TSO_DSO: 'T33_optToolPlanTsoDso',
  T51_CHARACTERIZATION: 'T51_characterization',
  T51_MONITORING: 'T51_monitoring',
  T52_INDICATOR: 'T52_indicator',
  T53_MANAGEMENT: 'T53_management',
};

export const TOOLS_INFO = {
  T25_DAY_AHEAD_TOOL: {
    name: 'T25_dayahead',
    path: '/tools/t25-dayahead',
    description: 'T2.5 - Day Ahead Aggregator tool',
  },

  T25_REAL_TIME_TOOL: {
    name: 'T25_realtime',
    path: '/tools/t25-realtime',
    description: 'T2.5 - Real Time Optimisation Tool',
  },

  T26_MARKET_SIMUL: {
    name: 'T26_marketSimul',
    path: '',
    description: 'T2.6 - Market Simulator',
  },

  T31_OPT_TOOL_DX: {
    name: 'T31_optToolDx',
    path: '',
    description: 'T3.1 - Optimisation Tool for distribution network planning',
  },

  T32_OPT_TOOL_TX: {
    name: 'T32_optToolTx',
    path: '',
    description: 'T3.2 - Optimisation Tool for transmission network planning',
  },

  T33_OPT_TOOL_PLAN_TSO_DSO: {
    name: 'T33_optToolPlanTsoDso',
    path: '',
    description: 'T3.3 - Optimisation tool for planning TSO/DSO shared technologies',
  },

  T41_WIND_AND_PV: {
    name: 'T41_windpv',
    path: '/tools/sgt',
    description: 'TSG - Scenario Generation Tool',
  },

  T41_STOCHASTIC_MP_ACOPF: {
    name: 'T41_stochasticmp',
    path: '',
    description: '',
  },

  T41_TRACTABILITY: {
    name: 'T41_Tractability_Tool',
    path: '/tools/t41',
    description: 'T4.1 - Tool for ancillary services activation in day ahead (DX) ',
  },

  T42_AS_REAL_TIME_DX: {
    name: 'T42_asRealTimeDx',
    path: '',
    description: 'T4.2 - Tool for ancillary services activation in real time (DX) ',
  },

  T43_STATE_ESTIMATOR: {
    name: 'T43_stateEstimator',
    path: '',
    description: 'T4.3 - State Estimator Tool',
  },

  T44_AS_DAY_HEAD_TX: {
    name: 'T44_asDayheadTx',
    path: '/tools/t44',
    description: 'T4.4 - Tool for ancillary services activation in day ahead (TX) ',
  },

  T45_AS_REAL_TIME_TX: {
    name: 'T45_asRealTimeTx',
    path: '/tools/t45',
    description: 'T4.5 - Tool for ancillary services activation in real time (TX) ',
  },

  T46_ODSA: {
    name: 'T46_odsa',
    path: '',
    description: 'T4.6 - Tool For Online Dynamic Security Assessment',
  },

  T51_TOOLS: {
    name: '',
    path: '',
    description: 'T5.1 - Assets Characterization and Monitoring Tools',
  },

  T51_CHARACTERIZATION: {
    name: 'T51_characterization',
    path: '',
    description: 'T5.1 - Assets Characterization Tool',
  },

  T51_MONITORING: {
    name: 'T51_monitoring',
    path: '/tools/t51',
    description: 'T5.1 - Assets Monitoring Tool',
  },

  T52_INDICATOR: {
    name: 'T52_indicator',
    path: '',
    description: 'T5.2 - Tool for the definition of condition indicators',
  },

  T53_MANAGEMENT: {
    name: 'T53_management',
    path: '',
    description: 'T5.3 - Tool for the definition of smart asset management strategies',
  },
};
