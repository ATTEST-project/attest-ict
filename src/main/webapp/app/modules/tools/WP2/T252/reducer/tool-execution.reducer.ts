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
  files: File[];
  jsonConfig: string;
}

const apiUrl = 'api/tools/wp2/run';

export const runT252Tool = createAsyncThunk(
  't252_tool/run',
  async ({ networkId, toolName, files, jsonConfig }: ToolRunParams) => {
    const formData = new FormData();
    formData.append('networkId', networkId.toString());
    formData.append('toolName', toolName);
    for (let i = 0; i < files?.length; ++i) {
      formData.append('files', files[i]);
    }
    formData.append('jsonConfig', jsonConfig);
    return await axios.post<any>(apiUrl, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  { serializeError: serializeAxiosError }
);

export const T252ToolExecutionSlice = createEntitySlice({
  name: 't252_tool_run',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(runT252Tool), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(runT252Tool), (state, action) => {
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

export const { reset } = T252ToolExecutionSlice.actions;

export default T252ToolExecutionSlice.reducer;
