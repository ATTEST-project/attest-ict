export interface NetworkSLDChartModel {
  options?: {
    title?: string;
    legend?: string[];
    yaxis?: {
      title: string;
    };
    xaxis?: {
      title: string;
      labels: string[];
    };
  };
  datasets?: {
    label?: string;
    values?: number[];
  }[];
}

export const defaultValueChart: Readonly<NetworkSLDChartModel> = {};
