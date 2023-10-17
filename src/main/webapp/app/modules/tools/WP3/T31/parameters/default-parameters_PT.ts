export const defaultParametersPT: Readonly<any> = {
  line_capacities: [
    0.045, 0.075, 0.1125, 0.15, 0.225, 0.3, 0.5, 0.75, 1.0, 2.0, 5.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0, 250.0, 500.0,
  ],
  TRS_capacities: [1.0, 2.0, 5.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0, 250.0, 500.0],
  line_costs: [],
  TRS_costs: [],
  cont_list: [],
  line_length: [],
  growth: {
    Active: { '2020': 0, '2030': 1.69, '2040': 2.29, '2050': 1.87 },
    Slow: { '2020': 0, '2030': 0.66, '2040': 1.32, '2050': 1.16 },
  },
  DSR: { Active: { '2020': 0, '2030': 0.0, '2040': 0.0, '2050': 0.0 }, Slow: { '2020': 0, '2030': 0.0, '2040': 0.0, '2050': 0.0 } },
  cluster: null,
  oversize: 0,
  Max_clusters: 4,
  scenarios: [],
  use_load_data_update: true,
  add_load_data_case_name: 'PT_Dx_01_',
};
