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
  filesDesc?: string[];
  files?: File[];
  parameterNames?: string[];
  parameterValues?: string[];
  profileIds?: number[];
}

const apiUrl = 'api/tools/wp4/run';

const generateFormData = ({ networkId, toolName, filesDesc, files, parameterNames, parameterValues, profileIds }: ToolRunParams) => {
  const formData = new FormData();
  formData.append('networkId', networkId.toString());
  formData.append('toolName', toolName);
  for (let i = 0; i < filesDesc?.length; ++i) {
    formData.append('filesDesc', filesDesc[i]);
  }
  for (let i = 0; i < files?.length; ++i) {
    formData.append('files', files[i]);
  }
  if (parameterNames) {
    if (parameterNames.length === 0) {
      formData.append('parameterNames', '');
    }
    for (let i = 0; i < parameterNames.length; ++i) {
      formData.append('parameterNames', parameterNames[i]);
    }
  }
  if (parameterValues) {
    if (parameterValues.length === 0) {
      formData.append('parameterValues', '');
    }
    for (let i = 0; i < parameterValues.length; ++i) {
      formData.append('parameterValues', parameterValues[i]);
    }
  }
  if (profileIds) {
    for (let i = 0; i < profileIds.length; ++i) {
      formData.append('profilesId', profileIds[i].toString());
    }
  }
  return formData;
};

export const runTool = createAsyncThunk(
  'tools/run',
  async ({ networkId, toolName, filesDesc, files, parameterNames, parameterValues, profileIds }: ToolRunParams) => {
    const formData = generateFormData({
      networkId,
      toolName,
      filesDesc,
      files,
      parameterNames,
      parameterValues,
      profileIds,
    });
    return await axios.post<any>(apiUrl, formData, {
      headers: { 'content-type': 'multipart/form-data' },
    });
  },
  { serializeError: serializeAxiosError }
);

export const ToolExecutionSlice = createEntitySlice({
  name: 'tools_run',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(runTool), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(runTool), (state, action) => {
        const { files, filesDesc, ...rest } = action.meta.arg;
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

export const { reset } = ToolExecutionSlice.actions;

export default ToolExecutionSlice.reducer;
