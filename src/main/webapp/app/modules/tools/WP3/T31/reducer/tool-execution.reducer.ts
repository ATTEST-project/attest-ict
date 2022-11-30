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

interface ToolRunParams {
  networkId: string | number;
  toolName: string;
  line_capacities: number[];
  TRS_capacities: number[];
  line_costs: number[];
  TRS_costs: number[];
  cont_list: number[];
  line_length: number[];
  growth: any;
  DSR: any;
  oversize: number;
  Max_clusters: number;
  scenarios: any[];
  cluster: any;
}

const apiUrl = 'api/tools/wp3/run';

export const runT31Tool = createAsyncThunk(
  't31_tool/run',
  async ({
    networkId,
    toolName,
    line_capacities,
    TRS_capacities,
    line_costs,
    TRS_costs,
    cont_list,
    line_length,
    growth,
    DSR,
    oversize,
    Max_clusters,
    scenarios,
    cluster,
  }: ToolRunParams) => {
    const jsonData = {
      networkId,
      toolName,
      line_capacities,
      TRS_capacities,
      line_costs,
      TRS_costs,
      cont_list,
      line_length,
      growth,
      DSR,
      oversize,
      Max_clusters,
      scenarios,
      cluster,
    };
    return await axios.post<any>(apiUrl, jsonData, {
      headers: { 'Content-Type': 'application/json' },
    });
  },
  { serializeError: serializeAxiosError }
);

export const T31ToolExecutionSlice = createEntitySlice({
  name: 't31_tool_run',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(runT31Tool), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(runT31Tool), (state, action) => {
        state.loading = false;
        state.updateSuccess = true;
        state.entity = {
          args: { ...action.meta.arg },
          status: action.payload.data.status,
          simulationId: action.payload.data.uuid,
        };
      });
  },
});

export const { reset } = T31ToolExecutionSlice.actions;

export default T31ToolExecutionSlice.reducer;
