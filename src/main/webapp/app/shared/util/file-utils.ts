export const SECTION = {
  MATPOWER: 'matpower',
  NETWORK: 'network',
  GENERATOR: 'generator',
  LOAD: 'load',
  FLEXIBILITY: 'flexibility',
  ALL: 'all',
  TOOL: 'tool',
};

export const FILE_TYPE_EXT = {
  MATPOWER: { mimeType: 'application/octet-stream', ext: '.m' },
  ODS: { mimeType: 'application/vnd.oasis.opendocument.spreadsheet', ext: '.ods' },
  EXCEL: { mimeType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', ext: '.xlsx' },
  ZIP: { mimeType: 'application/zip', ext: '.zip' },
  JSON: { mimeType: 'application/json', ext: '.json' },
  XLS: { mimeType: 'application/vnd.ms-excel', ext: '.xls' },
};

export const getFileTypeAndExtension = (contentType: string) => {
  return Object.values(FILE_TYPE_EXT).find(v => v.mimeType === contentType);
};

export const extractSubPathFromToolNum = (toolNum: string, fullPath: string): string | null => {
  // e.g
  // toolNum= "T45" and fullPath = "\ATSIM\WP4\T45\a04a5f2b-e880-4fba-a783-cdf528242f56\HR_Tx_01_2050.xlsx"
  if (toolNum != null && fullPath != null) {
    const length = toolNum.length;
    const indexOfToolNum = fullPath.lastIndexOf(toolNum);

    if (indexOfToolNum === -1) {
      return null;
    }
    return fullPath.slice(indexOfToolNum + length + 1); // return a04a5f2b-e880-4fba-a783-cdf528242f56\HR_Tx_01_2050.xlsx
  }
  return null;
};

export const extractFileName = (fullPath: string): string | null => {
  // e.g
  // toolNum= "T45" and fullPath = "\ATSIM\WP4\T45\a04a5f2b-e880-4fba-a783-cdf528242f56\HR_Tx_01_2050.xlsx"
  if (fullPath != null) {
    const index = fullPath.lastIndexOf('\\');
    if (index === -1) {
      return null;
    }
    return fullPath.slice(index + 1); // return a04a5f2b-e880-4fba-a783-cdf528242f56\HR_Tx_01_2050.xlsx
  }
  return null;
};
