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

interface T252ToolResultsParams {
  networkId: string | number;
  toolName: string;
  simulationId: string;
}

const apiDownloadUrl = 'api/tools/wp3/download';

export const downloadResults = createAsyncThunk(
  't252_tool/download-results',
  async ({ networkId, toolName, simulationId }: T252ToolResultsParams) => {
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
  },
  { serializeError: serializeAxiosError }
);

export const T252ToolResultsSlice = createEntitySlice({
  name: 't252_tool_results',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(downloadResults), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(downloadResults), (state, action) => {
        state.loading = false;
        state.updateSuccess = false;
      });
  },
});

export const { reset } = T252ToolResultsSlice.actions;

export default T252ToolResultsSlice.reducer;
