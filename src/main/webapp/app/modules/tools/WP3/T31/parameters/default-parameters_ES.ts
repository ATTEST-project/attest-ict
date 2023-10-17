export const defaultParametersES: Readonly<any> = {
  line_capacities: [
    0.045, 0.075, 0.1125, 0.15, 0.225, 0.3, 0.5, 0.75, 1.0, 2.0, 5.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0, 250.0, 500.0,
  ],
  TRS_capacities: [1.0, 2.0, 5.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0, 250.0, 500.0],
  line_costs: [],
  TRS_costs: [],
  cont_list: [],
  line_length: [],
  growth: { Active: { '2020': 0, '2030': 0.2, '2040': 2.0, '2050': 3.0 }, Slow: { '2020': 0, '2030': 0.1, '2040': 1.0, '2050': 2.0 } },
  DSR: { Active: { '2020': 0, '2030': 0.0, '2040': 0.0, '2050': 0.0 }, Slow: { '2020': 0, '2030': 0.0, '2040': 0.0, '2050': 0.0 } },
  cluster: null,
  oversize: 0,
  Max_clusters: 4,
  scenarios: [],
  use_load_data_update: false,
};
