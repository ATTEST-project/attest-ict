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

const apiUrl = 'api/tools/wp5/file-headers';

export const getFileHeader = createAsyncThunk(
  't512_tool/get_file_header',
  async (file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    return await axios.post<string[]>(apiUrl, formData, {
      headers: { 'content-type': 'multipart/form-data' },
    });
  },
  { serializeError: serializeAxiosError }
);

export const T512ToolFileHeaderSlice = createEntitySlice({
  name: 't512_tool_file_header',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(getFileHeader), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(getFileHeader), (state, action) => {
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      });
  },
});

export const { reset } = T512ToolFileHeaderSlice.actions;

export default T512ToolFileHeaderSlice.reducer;
