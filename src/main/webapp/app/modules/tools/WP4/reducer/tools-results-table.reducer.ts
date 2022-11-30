import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import axios from 'axios';
import { createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: null,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

interface WP4ToolResultsParams {
  networkId: string | number;
  toolName: string;
  simulationId: string;
  type?: string;
  nSc?: number;
  nConting?: number;
}

const apiTableUrl = 'api/tools/wp4/show-table';

export const showTable = createAsyncThunk(
  'tools/show_table',
  async ({ networkId, toolName, simulationId }: WP4ToolResultsParams) => {
    const params = {
      networkId: networkId.toString(),
      toolName,
      uuid: simulationId,
    };
    return await axios.get<any>(apiTableUrl, { params });
  },
  { serializeError: serializeAxiosError }
);

export const ToolsResultsTableSlice = createEntitySlice({
  name: 'tools_results_table',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(showTable), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(showTable), (state, action) => {
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      });
  },
});

export const { reset } = ToolsResultsTableSlice.actions;

export default ToolsResultsTableSlice.reducer;
