import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import axios from 'axios';
import { createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { getFileTypeAndExtension } from 'app/shared/util/file-utils';

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: null,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

interface T33ToolResultsParams {
  networkId: string | number;
  toolName: string;
  simulationId: string;
  title?: string;
  node?: string;
  day?: string;
}

const apiChartsUrl = 'api/tools/wp3/T33/show-charts';

export const showResults = createAsyncThunk(
  'tools/show_results',
  async ({ networkId, toolName, simulationId, title, node, day }: T33ToolResultsParams) => {
    const params = {
      networkId: networkId.toString(),
      toolName,
      uuid: simulationId,
      ...(title && { title }),
      ...(node && { node }),
      ...(day && { day }),
    };
    return await axios.get<any>(apiChartsUrl, { params });
  },
  { serializeError: serializeAxiosError }
);

export const T33ToolResultsSlice = createEntitySlice({
  name: 't33_tools_results',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(showResults), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(showResults), (state, action) => {
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      });
  },
});

export const { reset } = T33ToolResultsSlice.actions;

export default T33ToolResultsSlice.reducer;
