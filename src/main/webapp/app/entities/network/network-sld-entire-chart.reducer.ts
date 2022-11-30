import { createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValueChart, NetworkSLDChartModel } from 'app/shared/model/network-sld-chart.model';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import axios from 'axios';

const initialState: EntityState<NetworkSLDChartModel> = {
  entities: [],
  entity: defaultValueChart,
  errorMessage: null,
  loading: false,
  updateSuccess: false,
  updating: false,
};

const genUrls = {
  allSeasons: '/api/chart/gen-seasons',
  allDays: '/api/chart/gen-days',
  allSeasonsForDay: 'api/chart/gen-seasons-day',
  allDaysForSeason: 'api/chart/gen-days-season',
};

const loadUrls = {
  allSeasons: '/api/chart/load-seasons',
  allDays: '/api/chart/load-days',
  allSeasonsForDay: 'api/chart/load-seasons-day',
  allDaysForSeason: 'api/chart/load-days-season',
};

const flexUrls = {
  allSeasons: '/api/chart/flex-seasons',
  allDays: '/api/chart/flex-days',
  allSeasonsForDay: 'api/chart/flex-seasons-day',
  allDaysForSeason: 'api/chart/flex-days-season',
};

const getGenApiUrl = ({ period, typicalDay, season }: { period: string; typicalDay?: string; season?: string }) => {
  if (period === 'season') {
    if (typicalDay) {
      return genUrls.allSeasonsForDay;
    }
    return genUrls.allSeasons;
  } else if (period === 'day') {
    if (season) {
      return genUrls.allDaysForSeason;
    }
    return genUrls.allDays;
  }
  throw new Error('Period not valid!');
};

const getLoadApiUrl = ({ period, typicalDay, season }: { period: string; typicalDay?: string; season?: string }) => {
  if (period === 'season') {
    if (typicalDay) {
      return loadUrls.allSeasonsForDay;
    }
    return loadUrls.allSeasons;
  } else if (period === 'day') {
    if (season) {
      return loadUrls.allDaysForSeason;
    }
    return loadUrls.allDays;
  }
  throw new Error('Period not valid');
};

const getFlexApiUrl = ({ period, typicalDay, season }: { period: string; typicalDay?: string; season?: string }) => {
  if (period === 'season') {
    if (typicalDay) {
      return flexUrls.allSeasonsForDay;
    }
    return flexUrls.allSeasons;
  } else if (period === 'day') {
    if (season) {
      return flexUrls.allDaysForSeason;
    }
    return flexUrls.allDays;
  }
  throw new Error('Period not valid');
};

const getApiUrl = ({ type, period, typicalDay, season }: { type: string; period: string; typicalDay?: string; season?: string }) => {
  switch (type) {
    case 'gen':
      return getGenApiUrl({ period, typicalDay, season });
    case 'load':
      return getLoadApiUrl({ period, typicalDay, season });
    case 'flex':
      return getFlexApiUrl({ period, typicalDay, season });
    default:
      throw new Error('Component Type not valid!');
  }
};

export const generateChartAllSeasonsOrDays = createAsyncThunk(
  'network/generate_chart_all_seasons_or_days',
  async ({ networkId, busNum, type, period }: { networkId: number; busNum: number; type: string; period: string }) => {
    const apiUrl = getApiUrl({ type, period });
    const requestUrl = `${apiUrl}/${networkId}/${busNum}`;
    return axios.get<NetworkSLDChartModel>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const generateChartSeason = createAsyncThunk(
  'network/generate_chart_season',
  async ({
    networkId,
    busNum,
    type,
    period,
    typicalDay,
  }: {
    networkId: number;
    busNum: number;
    type: string;
    period: string;
    typicalDay: string;
  }) => {
    /* eslint-disable-next-line no-console */
    console.log('Component Type: ', type);
    const apiUrl = getApiUrl({ type, period, typicalDay });
    const requestUrl = `${apiUrl}/${networkId}/${busNum}/${typicalDay}`;
    return axios.get<NetworkSLDChartModel>(requestUrl);
  }
);

export const generateChartDay = createAsyncThunk(
  'network/generate_chart_day',
  async ({
    networkId,
    busNum,
    type,
    period,
    season,
  }: {
    networkId: number;
    busNum: number;
    type: string;
    period: string;
    season: string;
  }) => {
    const apiUrl = getApiUrl({ type, period, season });
    const requestUrl = `${apiUrl}/${networkId}/${busNum}/${season}`;
    return axios.get<NetworkSLDChartModel>(requestUrl);
  }
);

export const NetworkSLDChartSlice = createEntitySlice({
  name: 'network_sld_chart',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(generateChartAllSeasonsOrDays, generateChartSeason, generateChartDay), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(generateChartAllSeasonsOrDays, generateChartSeason, generateChartDay), (state, action) => {
        return {
          ...state,
          loading: false,
          entity: action.payload.data,
        };
      })
      .addMatcher(isRejected(generateChartAllSeasonsOrDays, generateChartSeason, generateChartDay), state => {
        state.loading = false;
        state.updating = false;
        state.entity = defaultValueChart;
      });
  },
});

export const { reset } = NetworkSLDChartSlice.actions;

export default NetworkSLDChartSlice.reducer;
