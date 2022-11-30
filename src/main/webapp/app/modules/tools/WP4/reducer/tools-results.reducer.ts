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

interface WP4ToolResultsParams {
  networkId: string | number;
  toolName: string;
  simulationId: string;
  type?: string;
  nSc?: number;
  nConting?: number;
}

const apiChartsUrl = 'api/tools/wp4/show-charts';
const apiDownloadChartsUrl = 'api/tools/wp4/download';

export const showCharts = createAsyncThunk(
  'tools/show_charts',
  async ({ networkId, toolName, simulationId, type, nSc, nConting }: WP4ToolResultsParams) => {
    const params = {
      networkId: networkId.toString(),
      toolName,
      uuid: simulationId,
      ...(type && { type }),
      ...(nSc && { nSc }),
      ...(nConting && { nConting }),
    };
    return await axios.get<any>(apiChartsUrl, { params });
  },
  { serializeError: serializeAxiosError }
);

export const downloadResults = createAsyncThunk(
  'tools/download-results',
  async ({ networkId, toolName, simulationId }: WP4ToolResultsParams) => {
    const params = {
      networkId: networkId.toString(),
      toolName,
      uuid: simulationId,
    };
    return await axios.get<any>(apiDownloadChartsUrl, { params, responseType: 'blob' }).then(res => {
      const { mimeType, ext } = getFileTypeAndExtension(res.headers['content-type']);
      const file = new Blob([res.data], { type: mimeType });
      const url = window.URL.createObjectURL(file);
      const a = document.createElement('a');
      a.href = url;
      a.download = networkId + '_' + toolName + '_' + simulationId + ext;
      a.click();
    });
  },
  { serializeError: serializeAxiosError }
);

export const ToolsResultsSlice = createEntitySlice({
  name: 'tools_results',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(showCharts, downloadResults), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(showCharts), (state, action) => {
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(downloadResults), (state, action) => {
        state.loading = false;
        state.updateSuccess = false;
      });
  },
});

export const { reset } = ToolsResultsSlice.actions;

export default ToolsResultsSlice.reducer;
