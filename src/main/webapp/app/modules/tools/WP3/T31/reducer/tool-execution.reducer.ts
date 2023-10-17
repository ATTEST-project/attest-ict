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

interface T31ToolRunParams {
  networkId: string | number;
  toolName: string;
  files: File[];
  filesDesc: string[];
  jsonConfig: string;
}

const apiUrl = 'api/tools/wp3/run';

export const runT31Tool = createAsyncThunk(
  't31_tool/run',
  async ({ networkId, toolName, files, filesDesc, jsonConfig }: T31ToolRunParams) => {
    const formData = new FormData();
    formData.append('networkId', networkId.toString());
    formData.append('toolName', toolName);

    for (let i = 0; i < files?.length; ++i) {
      formData.append('files', files[i]);
    }

    for (let i = 0; i < filesDesc?.length; ++i) {
      formData.append('filesDesc', filesDesc[i]);
    }

    formData.append('jsonConfig', jsonConfig);

    return await axios.post<any>(apiUrl, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
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
        const { files, ...rest } = action.meta.arg;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = {
          args: { ...rest },
          status: action.payload.data.status,
          simulationId: action.payload.data.uuid,
        };
      });
  },
});

export const { reset } = T31ToolExecutionSlice.actions;

export default T31ToolExecutionSlice.reducer;
