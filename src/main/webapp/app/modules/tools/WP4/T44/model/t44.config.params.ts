export interface T44ConfigParamsI {
  flexibility?: number;
  profile?: number;
  year?: number;
  problem?: number;
  case_name?: string | null;
  network_file?: string | null;
  auxiliary_file?: string | null;
  scenario_file?: string | null;
  output_dir?: string | null;
}
export const defaultValue: Readonly<T44ConfigParamsI> = {};
