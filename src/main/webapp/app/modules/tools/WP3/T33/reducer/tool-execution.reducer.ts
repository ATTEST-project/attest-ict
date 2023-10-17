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

interface T33RunParams {
  networkId: string | number;
  toolName: string;
  files?: File[];
  jsonConfig: string;
}

const apiUrl = 'api/tools/wp3/t33/run';

const generateFormData = ({ networkId, toolName, files, jsonConfig }: T33RunParams) => {
  const formData = new FormData();
  formData.append('networkId', networkId.toString());
  formData.append('toolName', toolName);
  formData.append('files', files[0]);
  formData.append('jsonConfig', jsonConfig);
  return formData;
};

export const runT33 = createAsyncThunk(
  't33_tool/run',
  // async ({ networkId, toolName, filesDesc, files, parameterNames, parameterValues, profileIds }: ToolRunParams) => {
  async ({ networkId, toolName, files, jsonConfig }: T33RunParams) => {
    const formData = generateFormData({
      networkId,
      toolName,
      files,
      jsonConfig,
    });
    return await axios.post<any>(apiUrl, formData, {
      headers: { 'content-type': 'multipart/form-data' },
    });
  },
  { serializeError: serializeAxiosError }
);

export const T33ExecutionSlice = createEntitySlice({
  name: 't33_tool/run',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(runT33), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(runT33), (state, action) => {
        const { files, ...rest } = action.meta.arg;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = {
          args: rest,
          status: action.payload.data.status,
          simulationId: action.payload.data.uuid,
        };
      });
  },
});

export const { reset } = T33ExecutionSlice.actions;

export default T33ExecutionSlice.reducer;
