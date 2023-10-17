export interface T41ConfigParamsI {
  ntp: number;
  scenario: number;
  flex_apc: number;
  flex_oltc: number;
  oltc_bin: number;
  flex_adpf: number;
  flex_fl: number;
  fl_bin: number;
  flex_str: number;
  str_bin: number;
  with_flex: number;
  case_name: string;
  season: string;
  year: number;
  auxiliary_file: string;
  scenario_file: string;
  output_file: string;
  outlog_file: string;
  network_file: string;
}

export default T41ConfigParamsI;
