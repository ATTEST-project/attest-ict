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

interface T33ResultsTableChartsParams {
  networkId: string | number;
  toolName: string;
  simulationId: string;
  title?: string;
  node?: string;
  day?: string;
}

const apiUrl = 'api/tools/wp3/show-charts';
export const showTableAndCharts = createAsyncThunk(
  'tools/wp3/show-table-and-charts',
  async ({ networkId, toolName, simulationId, title, node, day }: T33ResultsTableChartsParams) => {
    const params = {
      networkId: networkId.toString(),
      toolName,
      uuid: simulationId,
      title,
      node,
      day,
    };
    return await axios.get<any>(apiUrl, { params });
  },
  { serializeError: serializeAxiosError }
);

export const T33ResultsTableChartsSlice = createEntitySlice({
  name: 't33_results_table_charts',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(showTableAndCharts), state => {
        state.loading = true;
        state.updating = true;
      })
      .addMatcher(isFulfilled(showTableAndCharts), (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
        state.updateSuccess = true;
        /* eslint-disable-next-line no-console */
        console.log('state.entity :  ' + state.entity);
      });
  },
});

export default T33ResultsTableChartsSlice.reducer;
