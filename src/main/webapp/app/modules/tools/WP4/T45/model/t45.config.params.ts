export interface T45ConfigParamsI {
  matpower_network_file: string;
  PV_production_profile_file: string;
  state_estimation_csv_file: string;
  flex_devices_tech_char_file: string;
  flexibity_devices_states_file: string;
  DA_curtailment_file: string;
  current_time_period: string;
  output_distribution_bus: string;
  out_file: string;
  with_flex: number;
  case_name: string;
  season: string;
  year: number;
}

export default T45ConfigParamsI;
