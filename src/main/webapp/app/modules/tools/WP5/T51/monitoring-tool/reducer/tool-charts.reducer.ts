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

interface ToolShowChartsParams {
  networkId: number | string;
  toolName: string;
  simulationId: string;
  fileName: string;
}

const apiUrl = 'api/tools/wp5/show-charts';

export const showCharts = createAsyncThunk(
  't512_tool/show_charts',
  async ({ networkId, toolName, simulationId, fileName }: ToolShowChartsParams) => {
    const params = {
      networkId,
      toolName,
      uuid: simulationId,
      fileName,
    };
    return await axios.get<string>(apiUrl, { params });
  },
  { serializeError: serializeAxiosError }
);

export const T512ToolShowChartsSlice = createEntitySlice({
  name: 't512_tool_show_charts',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(showCharts), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(showCharts), (state, action) => {
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      });
  },
});

export const { reset } = T512ToolShowChartsSlice.actions;

export default T512ToolShowChartsSlice.reducer;
