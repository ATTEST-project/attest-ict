import { createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValueSubstations, NetworkSubstationsSLDProps } from 'app/shared/model/network-sld.model';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import axios from 'axios';

const initialStateSubstations: EntityState<NetworkSubstationsSLDProps> = {
  entities: [],
  entity: defaultValueSubstations,
  errorMessage: null,
  loading: false,
  updateSuccess: false,
  updating: false,
};

const apiSubstationsSLDUrl = '/api/sld-substations';

export const generateSubstationsSLD = createAsyncThunk(
  'network/generate_substations_sld',
  async (name: string) => {
    const requestUrl = `${apiSubstationsSLDUrl}/${name}`;
    return axios.get<NetworkSubstationsSLDProps>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const generateSubstationsSLDByNetworkId = createAsyncThunk(
  'network/generate_substations_sld_by_network_id',
  async (id: number | string) => {
    const requestUrl = `${apiSubstationsSLDUrl}/id/${id}`;
    return axios.get<NetworkSubstationsSLDProps>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const NetworkSLDSubstationsSlice = createEntitySlice({
  name: 'network_sld_substations',
  initialState: initialStateSubstations,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(generateSubstationsSLD, generateSubstationsSLDByNetworkId), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(generateSubstationsSLD, generateSubstationsSLDByNetworkId), (state, action) => {
        return {
          ...state,
          loading: false,
          entity: action.payload.data,
        };
      })
      .addMatcher(isRejected(generateSubstationsSLD, generateSubstationsSLDByNetworkId), (state, action: any) => {
        return {
          ...state,
          loading: false,
          entity: defaultValueSubstations,
          errorMessage: action.error?.response?.data?.message,
        };
      });
  },
});

export const { reset } = NetworkSLDSubstationsSlice.actions;

export default NetworkSLDSubstationsSlice.reducer;
