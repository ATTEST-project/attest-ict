import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { createEntitySlice, EntityState } from 'app/shared/reducers/reducer.utils';
import { defaultValue, INetwork } from 'app/shared/model/network.model';
import axios from 'axios';
import { MAX_SIZE } from 'app/shared/util/pagination.constants';

const initialState: EntityState<INetwork> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

interface SearchQueryParams {
  country: string;
  type: string;
  name?: string;
  mpcName?: string;
  fromNetworkDate?: string;
  toNetworkDate?: string;
}

const apiUrl = 'api/networks';

export const getEntities = createAsyncThunk(
  'network/search',
  async ({ country, type, name, mpcName, fromNetworkDate, toNetworkDate }: SearchQueryParams) => {
    const params = {
      'country.equals': country,
      'type.equals': type,
      size: MAX_SIZE,
      ...(name && { 'name.contains': name }),
      ...(mpcName && { 'mpcName.contains': mpcName }),
      ...(fromNetworkDate && { 'networkDate.greaterThanOrEqual': new Date(fromNetworkDate).toISOString() }),
      ...(toNetworkDate && { 'networkDate.lessThanOrEqual': new Date(toNetworkDate).toISOString() }),
    };
    // const requestUrl = `${apiUrl}?country.equals=${country}&mpcName.contains=${mpcName}&networkDate.greaterThan=${fromNetworkDate}&networkDate.lessThan=${toNetworkDate}&type.equals=${type}`;
    return axios.get<INetwork[]>(apiUrl, {
      params,
    });
  }
);

export const NetworkSearchSlice = createEntitySlice({
  name: 'network',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(getEntities), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
          totalItems: parseInt(action.payload.headers['x-total-count'], 10),
        };
      });
  },
});

export const { reset } = NetworkSearchSlice.actions;

export default NetworkSearchSlice.reducer;
