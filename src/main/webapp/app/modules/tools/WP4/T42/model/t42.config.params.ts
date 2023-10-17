export interface T42ConfigParamsI {
  matpower_network_file: string;
  PV_production_profile_file: string;
  state_estimation_csv_file: string;
  flex_devices_tech_char_file: string;
  flexibity_devices_states_file: string;
  trans_activation_file: string;
  out_file: string;
  current_time_period: string;
  with_flex: number;
  case_name: string;
  season: string;
  year: number;
}

export default T42ConfigParamsI;
