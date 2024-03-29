import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import axios from 'axios';
import { createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: null,
  updating: false,
  updateSuccess: false,
};

interface T41ResultsSelectParams {
  networkId: string | number;
  toolName: string;
  simulationId: string;
}

const apiTableUrl = 'api/tools/wp4/show-table';

export const showResultsToSelect = createAsyncThunk(
  'api/tools/wp4/t41/show_table', // type
  async ({ networkId, toolName, simulationId }: T41ResultsSelectParams) => {
    const params = {
      networkId: networkId.toString(),
      toolName,
      uuid: simulationId,
    };
    return await axios.get<any>(apiTableUrl, { params });
  },
  { serializeError: serializeAxiosError }
);

export const T41ResultsSelectSlice = createEntitySlice({
  name: 't41_results_select',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(showResultsToSelect), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(showResultsToSelect), (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
        state.updateSuccess = true;
      });
  },
});

export const { reset } = T41ResultsSelectSlice.actions;

export default T41ResultsSelectSlice.reducer;
