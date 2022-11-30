import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
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

interface NetworkUploadFileParams {
  networkName: string;
  file: File;
}

const apiUrl = 'api/upload-network';

export const uploadNetworkFile = createAsyncThunk(
  'network/upload_file',
  async ({ networkName, file }: NetworkUploadFileParams) => {
    const formData = new FormData();
    formData.append('networkName', networkName);
    formData.append('file', file);
    return await axios.post<any>(apiUrl, formData, {
      headers: { 'content-type': 'multipart/form-data' },
    });
  },
  { serializeError: serializeAxiosError }
);

export const NetworkMatpowerUploadSlice = createEntitySlice({
  name: 'network_matpower_upload',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(uploadNetworkFile), state => {
        state.loading = true;
      })
      .addMatcher(isFulfilled(uploadNetworkFile), (state, action) => {
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isRejected(uploadNetworkFile), state => {
        state.loading = false;
        state.updateSuccess = false;
        state.errorMessage = 'Cannot upload Matpower file';
        state.entity = null;
      });
  },
});

export const { reset } = NetworkMatpowerUploadSlice.actions;

export default NetworkMatpowerUploadSlice.reducer;
