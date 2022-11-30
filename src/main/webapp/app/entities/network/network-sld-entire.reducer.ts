import { createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import axios from 'axios';
import { defaultValueEntire, NetworkEntireSLDProps } from 'app/shared/model/network-sld.model';

const initialStateEntire: EntityState<NetworkEntireSLDProps> = {
  entities: [],
  entity: defaultValueEntire,
  errorMessage: null,
  loading: false,
  updateSuccess: false,
  updating: false,
};

const apiEntireSLDUrl = '/api/v1/sld-network';

export const generateEntireSLD = createAsyncThunk(
  'network/generate_entire_sld',
  async (name: string) => {
    const requestUrl = `${apiEntireSLDUrl}/${name}`;
    return axios.get<NetworkEntireSLDProps>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const generateEntireSLDById = createAsyncThunk(
  'network/generate_entire_sld_by_id',
  async (id: number) => {
    const requestUrl = `${apiEntireSLDUrl}/id/${id}`;
    return axios.get<NetworkEntireSLDProps>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const NetworkEntireSLDSlice = createEntitySlice({
  name: 'network_sld',
  initialState: initialStateEntire,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(generateEntireSLD, generateEntireSLDById), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(generateEntireSLD, generateEntireSLDById), (state, action) => {
        return {
          ...state,
          loading: false,
          entity: action.payload.data,
        };
      });
  },
});

export const { reset } = NetworkEntireSLDSlice.actions;

export default NetworkEntireSLDSlice.reducer;
