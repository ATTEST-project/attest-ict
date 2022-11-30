const increaseTimeoutForUrls = [
  {
    method: 'delete',
    url: 'api/networks',
  },
  {
    method: 'post',
    url: 'api/excel',
  },
  {
    method: 'post',
    url: 'api/csv',
  },
];

const removeTimeoutForUrls = [
  {
    method: 'post',
    url: 'api/tools/run',
  },
];

export const setTimeoutForUrls = config => {
  let timeout = config.timeout;
  if (increaseTimeoutForUrls.some(item => item.method === config.method.toLowerCase() && config.url.includes(item.url))) {
    timeout = 5 * 60 * 1000; // 5 minutes
  } else if (removeTimeoutForUrls.some(item => item.method === config.method.toLowerCase() && config.url.includes(item.url))) {
    timeout = 0; // timeout disabled
  }
  return timeout;
};
