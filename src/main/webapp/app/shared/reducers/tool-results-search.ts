import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import axios from 'axios';
import { defaultValue as filterDefaultValue, IToolResultsFilter } from 'app/shared/model/tool-results-filter.model';
import { ITool } from 'app/shared/model/tool.model';

import { defaultValue, IToolResult } from 'app/shared/model/tool-results.model';

const initialState = {
  toolResults: null,
  loading: false,
  errorMessage: null,
  toolList: [],
};

interface QueryParams {
  num: string;
}

const apiToolResultsUrl = 'api/tool-results';

const toolsApiUrl = 'api/tools';

// -- Actions
export const getToolByNum = createAsyncThunk(
  'tool-results/fetch-tool-by-num',
  async (num: QueryParams, thunkAPI) => {
    const filterToolNum = {
      'num.equals': num,
    };
    return await axios.get<ITool[]>(toolsApiUrl, { params: filterToolNum });
  },
  { serializeError: serializeAxiosError }
);

export const getTools = createAsyncThunk(
  'tool-results/fetch-tools',
  async () => {
    return await axios.get<ITool[]>(toolsApiUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getToolResults = createAsyncThunk(
  'tool-results/fetch-tool-results',
  async (filter: IToolResultsFilter, thunkAPI) => {
    return await axios.get<IToolResult[]>(apiToolResultsUrl, { params: filter });
  },
  { serializeError: serializeAxiosError }
);

export type ToolResultsSearchState = Readonly<typeof initialState>;

export const ToolResultsSearchSlice = createSlice({
  name: 'toolResultsSearch',
  initialState: initialState as ToolResultsSearchState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getToolResults.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(getToolResults.rejected, (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      })
      .addCase(getToolResults.fulfilled, (state, action) => {
        state.loading = false;
        state.toolResults = action.payload.data;
      })
      .addCase(getToolByNum.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(getToolByNum.rejected, (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      })
      .addCase(getToolByNum.fulfilled, (state, action) => {
        state.loading = false;
        state.toolList = action.payload.data;
      })
      .addCase(getTools.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(getTools.rejected, (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      })
      .addCase(getTools.fulfilled, (state, action) => {
        state.loading = false;
        state.toolList = action.payload.data;
      });
  },
});

export const { reset } = ToolResultsSearchSlice.actions;

export default ToolResultsSearchSlice.reducer;
