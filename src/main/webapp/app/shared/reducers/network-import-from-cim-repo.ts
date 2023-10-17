import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import axios from 'axios';
import { INetwork } from 'app/shared/model/network.model';
import { ICimRepoNetwork } from 'app/shared/model/cim-repo-network.model';

const initialState = {
  country: 'HR',
  type: '',
  cimNetworks: [],
  caseName: '',
  loading: false,
  importSuccess: false,
  networkIdUploaded: [],
  errorMessage: null,
};

const apiUrl = 'api/cimRepoNetworks';

// -- Actions
export const getCimRepoNetworks = createAsyncThunk(
  'network-cim-repo/fetch_cim_networks',
  async () => {
    return await axios.get<INetwork[]>(apiUrl);
  },
  { serializeError: serializeAxiosError }
);

export const importCimNetwork = createAsyncThunk(
  'network-cim-repo/import_network',
  async (cimNetwork: ICimRepoNetwork, thunkAPI) => {
    const result = await axios.post<ICimRepoNetwork>(apiUrl, cimNetwork);
    return result;
  },
  { serializeError: serializeAxiosError }
);

export type NetworkImportFromCimRepoState = Readonly<typeof initialState>;

export const NetworkImportFromCimRepoSlice = createSlice({
  name: 'networkImportCim',
  initialState: initialState as NetworkImportFromCimRepoState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getCimRepoNetworks.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(getCimRepoNetworks.rejected, (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      })
      .addCase(getCimRepoNetworks.fulfilled, (state, action) => {
        state.loading = false;
        state.cimNetworks = action.payload.data;
      })
      .addCase(importCimNetwork.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(importCimNetwork.rejected, (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      })
      .addCase(importCimNetwork.fulfilled, (state, action) => {
        state.importSuccess = true;
        state.loading = false;
      });
  },
});

export const { reset } = NetworkImportFromCimRepoSlice.actions;

export default NetworkImportFromCimRepoSlice.reducer;
