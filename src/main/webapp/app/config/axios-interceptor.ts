import axios from 'axios';
import { Storage } from 'react-jhipster';
import getStore from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { setTimeoutForUrls } from 'app/config/axios-interceptor-util';

/* const TIMEOUT = 1 * 60 * 1000;
axios.defaults.timeout = TIMEOUT; */
axios.defaults.baseURL = SERVER_API_URL;

const store = getStore();

const setupAxiosInterceptors = onUnauthenticated => {
  const onRequestSuccess = config => {
    const authentication = store.getState().authentication;
    if (authentication.isAuthenticated && config.method.toLowerCase() === 'get' && config.url.includes('api/networks')) {
      config.params = config.params || {};
      if (![AUTHORITIES.DSO, AUTHORITIES.TSO].every(role => authentication.account.authorities.includes(role))) {
        if (hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.DSO])) {
          config.params['type.equals'] = 'DX';
        } else if (hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.TSO])) {
          config.params['type.equals'] = 'TX';
        }
      }
    }
    config.timeout = setTimeoutForUrls(config);
    return config;
  };
  const onResponseSuccess = response => response;
  const onResponseError = err => {
    const status = err.status || (err.response ? err.response.status : 0);
    if (status === 403 || status === 401) {
      onUnauthenticated();
    }
    return Promise.reject(err);
  };
  axios.interceptors.request.use(onRequestSuccess);
  axios.interceptors.response.use(onResponseSuccess, onResponseError);
};

export default setupAxiosInterceptors;
