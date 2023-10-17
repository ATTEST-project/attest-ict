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

interface T45ResultsTableChartsParams {
  networkId: string | number;
  toolName: string;
  simulationId: string;
  title?: string;
}

const apiUrl = 'api/tools/wp4/show-charts';
export const showTableAndCharts = createAsyncThunk(
  'tools/wp4/show-table-and-charts',
  async ({ networkId, toolName, simulationId, title }: T45ResultsTableChartsParams) => {
    const params = {
      networkId: networkId.toString(),
      toolName,
      uuid: simulationId,
      title,
    };
    return await axios.get<any>(apiUrl, { params });
  },
  { serializeError: serializeAxiosError }
);

export const T45ResultsTableChartsSlice = createEntitySlice({
  name: 'T45_results_table_charts',
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

export default T45ResultsTableChartsSlice.reducer;
