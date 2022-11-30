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

interface ToolShowTableParams {
  networkId: number | string;
  toolName: string;
  simulationId: string;
}

const apiUrl = 'api/tools/wp5/show-table';
const apiDownloadUrl = 'api/tools/wp5/download';

export const showTable = createAsyncThunk(
  't53_tool/show_table',
  async ({ networkId, toolName, simulationId }: ToolShowTableParams) => {
    const params = {
      networkId,
      toolName,
      uuid: simulationId,
    };
    return await axios.get<string[]>(apiUrl, { params });
  },
  { serializeError: serializeAxiosError }
);

export const downloadResults = createAsyncThunk(
  't53_tool/download_results',
  async ({ networkId, toolName, simulationId }: ToolShowTableParams) => {
    const params = {
      networkId: networkId.toString(),
      toolName,
      uuid: simulationId,
    };
    return await axios.get<any>(apiDownloadUrl, { params, responseType: 'blob' }).then(res => {
      const { mimeType, ext } = getFileTypeAndExtension(res.headers['content-type']);
      const file = new Blob([res.data], { type: mimeType });
      const url = window.URL.createObjectURL(file);
      const a = document.createElement('a');
      a.href = url;
      a.download = networkId + '_' + toolName + '_' + simulationId + ext;
      a.click();
    });
  }
);

export const T53ToolShowTableSlice = createEntitySlice({
  name: 't53_tool_show_table',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(showTable, downloadResults), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(showTable), (state, action) => {
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(downloadResults), state => {
        state.loading = false;
        state.updateSuccess = true;
      });
  },
});

export const { reset } = T53ToolShowTableSlice.actions;

export default T53ToolShowTableSlice.reducer;
