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
  profileId?: number;
  files: File[];
  jsonConfig: string;
}

const apiUrl = 'api/tools/wp3/t32/run';

export const runT32Tool = createAsyncThunk(
  't32_tool/run',
  async ({ networkId, toolName, profileId, files, jsonConfig }: ToolRunParams) => {
    const formData = new FormData();
    formData.append('networkId', networkId.toString());
    formData.append('toolName', toolName);
    profileId && formData.append('profileId', profileId.toString());
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

export const T32ToolExecutionSlice = createEntitySlice({
  name: 't32_tool_run',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(runT32Tool), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(runT32Tool), (state, action) => {
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

export const { reset } = T32ToolExecutionSlice.actions;

export default T32ToolExecutionSlice.reducer;
