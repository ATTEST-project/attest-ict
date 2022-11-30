export const defaultParameters: Readonly<any> = {
  line_capacities: [
    0.045, 0.075, 0.1125, 0.15, 0.225, 0.3, 0.5, 0.75, 1.0, 2.0, 5.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0, 250.0, 500.0,
  ],
  TRS_capacities: [1.0, 2.0, 5.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0, 250.0, 500.0],
  line_costs: [],
  TRS_costs: [],
  cont_list: [],
  line_length: [],
  growth: { Active: { '2020': 0, '2030': 1.89, '2040': 3.0 }, Slow: { '2020': 0, '2030': 1.1, '2040': 2.0 } },
  DSR: { Active: { '2020': 0, '2030': 0.05, '2040': 0.05 }, Slow: { '2020': 0, '2030': 0.02, '2040': 0.02 } },
  cluster: null,
  oversize: 0,
  Max_clusters: 5,
  scenarios: [],
};
