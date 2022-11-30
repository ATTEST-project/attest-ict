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
};

export const getFileTypeAndExtension = (contentType: string) => {
  return Object.values(FILE_TYPE_EXT).find(v => v.mimeType === contentType);
};
