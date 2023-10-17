import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import T44ConfigParams from 'app/modules/tools/WP4/T44/results/config-params';

const initialState = {
  pages: [],
  scenariosOption: [],
  contingenciesOption: [],
  parameters: {},
  updateSuccess: false,
  loading: false,
  errorMessage: null,
};

interface T44ResultsSelectParams {
  networkId: string | number;
  toolName: string;
  uuid: string;
}

const apiTableUrl = 'api/tools/wp4/show-table';

export const showResultsToSelect = createAsyncThunk(
  'api/tools/wp4/t44/show_table', // type
  async ({ networkId, toolName, uuid }: T44ResultsSelectParams) => {
    const params = {
      networkId: networkId.toString(),
      toolName,
      uuid,
    };

    /* eslint-disable-next-line no-console */
    // console.log('T44 showResultsToSelect Reducer, uuid:' +uuid);
    return await axios.get<any>(apiTableUrl, { params });
  },
  { serializeError: serializeAxiosError }
);

export type T44ResultsSelectState = Readonly<typeof initialState>;

export const T44ResultsSelectSlice = createSlice({
  name: 't44_results_select',
  initialState: initialState as T44ResultsSelectState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(showResultsToSelect.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(showResultsToSelect.rejected, (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      })
      .addCase(showResultsToSelect.fulfilled, (state, action) => {
        state.updateSuccess = true;
        state.loading = false;
        state.pages = action.payload.data.pages;
        state.scenariosOption = action.payload.data.scenarios;
        state.contingenciesOption = action.payload.data.contingencies;
        state.parameters = action.payload.data.toolConfigParameters.parameters;
      });
  },
});

export const { reset } = T44ResultsSelectSlice.actions;

export default T44ResultsSelectSlice.reducer;
