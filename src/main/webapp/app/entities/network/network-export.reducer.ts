import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import axios from 'axios';
import { FILE_TYPE_EXT } from 'app/shared/util/file-utils';
import { INetwork } from 'app/shared/model/network.model';
import { createEntitySlice } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: null,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiExportToMatpowerUrl = '/api/export-data/';
const apiExportToODSUrl = '/api/ods/export/';

export const exportToMatpower = createAsyncThunk('network_export/matpower', async (network: INetwork) => {
  await axios.get(apiExportToMatpowerUrl + network.name, { responseType: 'arraybuffer' }).then(res => {
    const { mimeType, ext } = FILE_TYPE_EXT.MATPOWER;
    const file = new Blob([res.data], { type: mimeType });
    const url = window.URL.createObjectURL(file);
    const a = document.createElement('a');
    a.href = url;
    a.download = network.name + ext;
    a.click();
  });
});

export const exportToODS = createAsyncThunk('network_export/ods', async (network: INetwork) => {
  await axios.get(apiExportToODSUrl + network.id, { responseType: 'blob' }).then(res => {
    const { mimeType, ext } = FILE_TYPE_EXT.ODS;
    const file = new Blob([res.data], { type: mimeType });
    const url = window.URL.createObjectURL(file);
    const a = document.createElement('a');
    a.href = url;
    a.download = network.name + ext;
    a.click();
  });
});

export const NetworkExportSlice = createEntitySlice({
  name: 'network_export',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(exportToMatpower, exportToODS), state => {
        state.loading = true;
      })
      .addMatcher(isFulfilled(exportToMatpower, exportToODS), state => {
        state.loading = false;
        state.updateSuccess = true;
      })
      .addMatcher(isRejected(exportToMatpower, exportToODS), state => {
        state.loading = false;
        state.updateSuccess = false;
        state.errorMessage = 'Cannot export data!';
      });
  },
});

export const { reset } = NetworkExportSlice.actions;

export default NetworkExportSlice.reducer;
